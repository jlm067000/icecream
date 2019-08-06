package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.Result;
import com.cognizant.icecream.clients.ResultFactory;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import org.springframework.stereotype.Service;

@Service
public class GarageService {


    public Result resupply(String garageCode, TimeSlot timeSlot) {

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
