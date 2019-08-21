package com.cognizant.icecream.services;

import com.cognizant.icecream.pools.PoolKey;
import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.ResultFactory;
import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.SupplyClient;
import com.cognizant.icecream.clients.TimeClient;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import com.cognizant.icecream.result.Result;
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
    private ServiceResultPool resultPool;

    static {
        FUTURE = ResultFactory.createResult(false, "Resupply must be SCHEDULED for a FUTURE time slot.");
        SCHEDULED = ResultFactory.createResult(true, "SCHEDULED");
        COULD_NOT_RESUPPLY = ResultFactory.createResult(false, "Supply Service was not able to schedule resupply");
        REMOVED = ResultFactory.createResult(true, "REMOVED");
    }

    @Autowired
    public GarageService(GarageCRUD garageCRUD, SupplyClient supplyClient, TimeClient timeClient, ServiceResultPool resultPool) {
        this.garageCRUD = garageCRUD;
        this.supplyClient = supplyClient;
        this.timeClient = timeClient;
        this.resultPool = resultPool;
    }

    public Result resupply(String garageCode, TimeSlot timeSlot) {

        if(!timeClient.isValid(timeSlot)) {
            return FUTURE;
        }

        if(!garageCRUD.findByCode(garageCode).isPresent()) {

            String errMsg = String.format(NOT_FOUND, garageCode);
            return ResultFactory.createResult(false, errMsg);
        }

        boolean success = supplyClient.scheduleResupply(garageCode, timeSlot);

        return success ? SCHEDULED : COULD_NOT_RESUPPLY;
    }

    public <T> T getGarage(String garageCode, Function<ServiceResult, T> resultProcessor) throws Exception {

        Optional<Garage> garage = garageCRUD.findByCode(garageCode);

        return processOptional(garage, NOT_FOUND, garageCode, resultProcessor);
    }

    public <T> T addGarage(Garage garage, Function<ServiceResult, T> resultProcessor) throws Exception {

        Optional<Garage> added = garageCRUD.add(garage);

        return processOptional(added, COULD_NOT_ADD, garage.getCode(), resultProcessor);
    }

    public <T> T updateGarage(Garage garage, Function<ServiceResult, T> resultProcessor) throws Exception {

        Optional<Garage> updated = garageCRUD.update(garage);

        return processOptional(updated, COULD_NOT_UPDATE, garage.getCode(), resultProcessor);
    }

    public Result removeGarage(String garageCode) {

        boolean success = garageCRUD.remove(garageCode);

        if(success) {
            return REMOVED;
        }
        else {
            return ResultFactory.createResult(false, "Could not remove Garage: " + garageCode);
        }
    }

    private <T> T processOptional(
                    Optional<Garage> optional,
                    String formatErrStr,
                    String formatArg,
                    Function<ServiceResult, T> resultProcessor
    ) throws Exception
    {
        PoolKey<ServiceResult> key = processOptional(optional, formatErrStr, formatArg);
        return resultPool.processObject(key, resultProcessor);
    }


    private PoolKey<ServiceResult> processOptional(Optional<Garage> optional, String formatErrStr, String formatArg)
            throws Exception
    {
        if(optional.isPresent()) {
            return resultPool.createResult(true, null, optional.get());
        }
        else {
            String errMsg = String.format(formatErrStr, formatArg);
            return resultPool.createResult(false, errMsg, null);
        }
    }
}
