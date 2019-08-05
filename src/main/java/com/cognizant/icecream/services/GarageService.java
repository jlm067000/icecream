package com.cognizant.icecream.services;

import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import com.cognizant.icecream.clients.ResultObject;
import org.springframework.stereotype.Service;

@Service
public class GarageService {


    public ResultObject resupply(String garageCode, TimeSlot timeSlot) {

        return new ResultObject();
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

    public ResultObject removeGarage(String garageCode) {
        return new ResultObject();
    }

}
