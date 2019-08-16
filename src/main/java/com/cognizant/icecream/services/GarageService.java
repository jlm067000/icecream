package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.Result;
import com.cognizant.icecream.clients.ResultFactory;
import com.cognizant.icecream.clients.SupplyClient;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        boolean success = supplyClient.scheduleResupply(garageCode, timeSlot);

        if(success) {
            return ResultFactory.createResult(true, "scheduled");
        }
        else {
            return ResultFactory.createResult(false, "Supply Service was not able to schedule resupply");
        }
    }

    public Garage getGarage(String garageCode) {


        Optional<Garage> garage = garageCRUD.findByCode(garageCode);

        if(garage.isPresent()) {
            return garage.get();
        }
        else {
            return null;
        }
    }

    public Garage addGarage(Garage garage) {

        Optional<Garage> added = garageCRUD.add(garage);

        if(added.isPresent()) {
            return added.get();
        }
        else {
            return null;
        }
    }

    public Garage updateGarage(Garage garage) {

        Optional<Garage> updated = garageCRUD.update(garage);

        if(updated.isPresent()) {
            return updated.get();
        }
        else {
            return null;
        }
    }

    public Result removeGarage(String garageCode) {

        boolean success = garageCRUD.remove(garageCode);

        if(success) {
            return ResultFactory.createResult(true, "removed");
        }
        else {
            return ResultFactory.createResult(false, "Could not remove Garage: " + garageCode);
        }
    }
}
