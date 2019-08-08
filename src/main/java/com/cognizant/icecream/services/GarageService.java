package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.Result;
import com.cognizant.icecream.clients.ResultFactory;
import com.cognizant.icecream.clients.SupplyClient;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GarageService {

    private GarageCRUD garageCRUD;
    private SupplyClient supplyClient;

    @Autowired
    public GarageService(GarageCRUD garageCRUD, SupplyClient supplyClient) {
        this.garageCRUD = garageCRUD;
        this.supplyClient = supplyClient;
    }

    public Result resupply(String garageCode, TimeSlot timeSlot) {

        if(!garageCRUD.findByCode(garageCode).isPresent()) {
            return ResultFactory.createResult(false, "Garage " + garageCode + " not found");
        }

        return ResultFactory.createResult(true, "scheduled");
    }

    public Garage getGarage(String garageCode) {

        return new Garage();
    }

    public Garage addGarage(Garage garage) {
        return garage;
    }

    public Garage updateGarage(Garage garage) {
        return garage;
    }

    public Result removeGarage(String garageCode) {
        return ResultFactory.createResult(true, "removed");
    }
}
