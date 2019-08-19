package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.result.ClientResultFactory;
import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.SupplyClient;
import com.cognizant.icecream.clients.TimeClient;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import com.cognizant.icecream.models.result.ClientResult;
import com.cognizant.icecream.models.result.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GarageService {


    private static final ClientResult future;
    private static final ClientResult scheduled;
    private static final ClientResult couldNotResupply;
    private static final ClientResult removed;

    private GarageCRUD garageCRUD;
    private SupplyClient supplyClient;
    private TimeClient timeClient;

    static {
        future = ClientResultFactory.createResult(false, "Resupply must be scheduled for a future time slot.");
        scheduled = ClientResultFactory.createResult(true, "scheduled");
        couldNotResupply = ClientResultFactory.createResult(false, "Supply Service was not able to schedule resupply");
        removed = ClientResultFactory.createResult(true, "removed");
    }

    @Autowired
    public GarageService(GarageCRUD garageCRUD, SupplyClient supplyClient, TimeClient timeClient) {
        this.garageCRUD = garageCRUD;
        this.supplyClient = supplyClient;
        this.timeClient = timeClient;
    }

    public ServiceResult<Void> resupply(String garageCode, TimeSlot timeSlot) {

        if(!timeClient.isValid(timeSlot)) {
            return future;
        }

        if(!garageCRUD.findByCode(garageCode).isPresent()) {
            return ClientResultFactory.createResult(false, "Garage " + garageCode + " not found");
        }

        boolean success = supplyClient.scheduleResupply(garageCode, timeSlot);

        return success ? scheduled : couldNotResupply;
    }

    public ServiceResult<Garage> getGarage(String garageCode) {

        return garageCRUD.findByCode(garageCode);
    }

    public ServiceResult<Garage> addGarage(Garage garage) {

        return garageCRUD.add(garage);
    }

    public ServiceResult<Garage> updateGarage(Garage garage) {

        return garageCRUD.update(garage);
    }

    public ServiceResult<Void> removeGarage(String garageCode) {

        boolean success = garageCRUD.remove(garageCode);

        if(success) {
            return removed;
        }
        else {
            return ClientResultFactory.createResult(false, "Could not remove Garage: " + garageCode);
        }
    }
}
