package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.SupplyClient;
import com.cognizant.icecream.clients.TimeClient;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import com.cognizant.icecream.pools.api.ResultPool;
import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GarageService {

    private static final Result FUTURE;
    private static final Result SCHEDULED;
    private static final Result COULD_NOT_RESUPPLY;
    private static final Result REMOVED;
    private static final String NOT_FOUND = "Garage %s not found.";
    private static final String COULD_NOT_ADD = "Could not add Garage %s.";
    private static final String COULD_NOT_UPDATE = "Could not update Garage %s. Verify that this Garage has been added.";

    private GarageCRUD garageCRUD;
    private SupplyClient supplyClient;
    private TimeClient timeClient;
    private ServiceResultPool<Garage> serviceResultPool;
    private ResultPool resultPool;

    static {
        FUTURE = ResultFactory.createResult(false, "Resupply must be SCHEDULED for a FUTURE time slot.");
        SCHEDULED = ResultFactory.createResult(true, "SCHEDULED");
        COULD_NOT_RESUPPLY = ResultFactory.createResult(false, "Supply Service was not able to schedule resupply");
        REMOVED = ResultFactory.createResult(true, "REMOVED");
    }

    @Autowired
    public GarageService(
            GarageCRUD garageCRUD,
            SupplyClient supplyClient,
            TimeClient timeClient,
            ServiceResultPool<Garage> serviceResultPool,
            ResultPool resultPool
    ) {
        this.garageCRUD = garageCRUD;
        this.supplyClient = supplyClient;
        this.timeClient = timeClient;
        this.serviceResultPool = serviceResultPool;
        this.resultPool = resultPool;
    }

    public <T> T resupply(String authorization, String garageCode, TimeSlot timeSlot, ResultProcessor<T> resultProcessor) {

        if(!timeClient.isValid(authorization, timeSlot)) {
            return resultProcessor.apply(FUTURE);
        }

        if(!garageCRUD.findByCode(authorization, garageCode).isPresent()) {

            String errMsg = String.format(NOT_FOUND, garageCode);
            MutableResult result = ServicesUtil.createResult(false, errMsg, resultPool);

            return ServicesUtil.processResult(result, resultProcessor, resultPool);
        }

        boolean success = supplyClient.scheduleResupply(authorization, garageCode, timeSlot);

        return success ? resultProcessor.apply(SCHEDULED) : resultProcessor.apply(COULD_NOT_RESUPPLY);
    }

    public <T> T getGarage(String garageCode, ServiceResultProcessor<Garage, T> resultProcessor) {

        Optional<Garage> garage = garageCRUD.findByCode(garageCode);

        return ServicesUtil.processOptional(garage, NOT_FOUND, garageCode, serviceResultPool, resultProcessor);
    }

    public <T> T addGarage(Garage garage, ServiceResultProcessor<Garage, T> resultProcessor) {

        Optional<Garage> added = garageCRUD.add(garage);

        return ServicesUtil.processOptional(added, COULD_NOT_ADD, garage.getCode(), serviceResultPool, resultProcessor);
    }

    public <T> T updateGarage(Garage garage, ServiceResultProcessor<Garage, T> resultProcessor) {

        Optional<Garage> updated = garageCRUD.update(garage);

        return ServicesUtil.processOptional(updated, COULD_NOT_UPDATE, garage.getCode(), serviceResultPool, resultProcessor);
    }

    public <T> T removeGarage(String garageCode, ResultProcessor<T> resultProcessor) {

        boolean success = garageCRUD.remove(garageCode);

        if(success) {
            return resultProcessor.apply(REMOVED);
        }

        MutableResult result =
                ServicesUtil.createResult(false, "Could not remove Garage: " + garageCode, resultPool);

        return ServicesUtil.processResult(result, resultProcessor, resultPool);
    }
}
