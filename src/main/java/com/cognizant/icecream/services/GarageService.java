package com.cognizant.icecream.services;

import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import com.cognizant.icecream.services.api.Result;
import org.springframework.stereotype.Service;

@Service
public class GarageService {


    public Result resupply(String garageCode, TimeSlot timeSlot) {

        return new Result();
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

    public Result removeGarage(Garage garage) {
        return new Result();
    }

}
