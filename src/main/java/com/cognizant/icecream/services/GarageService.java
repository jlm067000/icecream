package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.SupplyClient;
import com.cognizant.icecream.clients.TimeClient;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import com.cognizant.icecream.pools.api.ResultPool;
import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.MutableServiceResult;
import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.result.ResultFactory;
import com.cognizant.icecream.result.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

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

    public Result resupply(String garageCode, TimeSlot timeSlot) {

        if(!timeClient.isValid(timeSlot)) {
            return FUTURE;
        }

        if(!garageCRUD.findByCode(garageCode).isPresent()) {

            String errMsg = String.format(NOT_FOUND, garageCode);
            return ServicesUtil.createResult(false, errMsg, resultPool);
        }

        boolean success = supplyClient.scheduleResupply(garageCode, timeSlot);

        return success ? SCHEDULED : COULD_NOT_RESUPPLY;
    }

    public <T> T getGarage(String garageCode, Function<ServiceResult, T> resultProcessor) {

        Optional<Garage> garage = garageCRUD.findByCode(garageCode);

        return processOptional(garage, NOT_FOUND, garageCode, resultProcessor);
    }

    public <T> T addGarage(Garage garage, Function<ServiceResult, T> resultProcessor) {

        Optional<Garage> added = garageCRUD.add(garage);

        return processOptional(added, COULD_NOT_ADD, garage.getCode(), resultProcessor);
    }

    public <T> T updateGarage(Garage garage, Function<ServiceResult, T> resultProcessor) {

        Optional<Garage> updated = garageCRUD.update(garage);

        return processOptional(updated, COULD_NOT_UPDATE, garage.getCode(), resultProcessor);
    }

    public Result removeGarage(String garageCode) {

        boolean success = garageCRUD.remove(garageCode);

        if(success) {
            return REMOVED;
        }
        else {
            return ServicesUtil.createResult(false, "Could not remove Garage: " + garageCode, resultPool);
        }
    }

    private <T> T processOptional(
                    Optional<Garage> optional,
                    String formatErrStr,
                    String formatArg,
                    Function<ServiceResult, T> resultProcessor
    ) {
        MutableServiceResult<Garage> result = processOptional(optional, formatErrStr, formatArg);
        T processed = resultProcessor.apply(result);
        serviceResultPool.returnObject(result);

        return processed;
    }

    private MutableServiceResult processOptional(Optional<Garage> optional, String formatErrStr, String formatArg) {

        if(optional.isPresent()) {
            return ServicesUtil.createResult(true, null, optional.get(), serviceResultPool);
        }
        else {
            String errMsg = String.format(formatErrStr, formatArg);
            return ServicesUtil.createResult(false, errMsg, null, serviceResultPool);
        }
    }
}
