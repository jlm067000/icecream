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

@Service
public class GarageService {


    private static final Result future;
    private static final Result scheduled;
    private static final Result couldNotResupply;
    private static final Result removed;

    private GarageCRUD garageCRUD;
    private SupplyClient supplyClient;
    private TimeClient timeClient;
    private ServiceResultPool resultPool;

    static {
        future = ResultFactory.createResult(false, "Resupply must be scheduled for a future time slot.");
        scheduled = ResultFactory.createResult(true, "scheduled");
        couldNotResupply = ResultFactory.createResult(false, "Supply Service was not able to schedule resupply");
        removed = ResultFactory.createResult(true, "removed");
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
            return future;
        }

        if(!garageCRUD.findByCode(garageCode).isPresent()) {
            return ResultFactory.createResult(false, "Garage " + garageCode + " not found");
        }

        boolean success = supplyClient.scheduleResupply(garageCode, timeSlot);

        return success ? scheduled : couldNotResupply;
    }

    public PoolKey<ServiceResult> getGarage(String garageCode) {

        return garageCRUD.findByCode(garageCode);
    }

    public PoolKey<ServiceResult> addGarage(Garage garage) {

        return garageCRUD.add(garage);
    }

    public PoolKey<ServiceResult> updateGarage(Garage garage) {

        return garageCRUD.update(garage);
    }

    public Result removeGarage(String garageCode) {

        boolean success = garageCRUD.remove(garageCode);

        if(success) {
            return removed;
        }
        else {
            return ResultFactory.createResult(false, "Could not remove Garage: " + garageCode);
        }
    }
}
