package com.cognizant.icecream.services;

import com.cognizant.icecream.api.result.ResultProcessor;
import com.cognizant.icecream.api.result.ServiceResultProcessor;
import com.cognizant.icecream.components.api.GarageCRUDOperator;
import com.cognizant.icecream.components.api.GarageResupplier;
import com.cognizant.icecream.api.models.Garage;
import com.cognizant.icecream.api.models.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GarageService {

    private GarageCRUDOperator crudOperator;
    private GarageResupplier resupplier;

    @Autowired
    public GarageService(GarageCRUDOperator crudOperator, GarageResupplier resupplier) {
        this.crudOperator = crudOperator;
        this.resupplier = resupplier;
    }

    public <T> T resupply(String authorization, String garageCode, TimeSlot timeSlot, ResultProcessor<T> resultProcessor) {

        return resupplier.resupply(authorization, garageCode, timeSlot, resultProcessor);
    }

    public <T> T getGarage(String garageCode, ServiceResultProcessor<Garage, T> resultProcessor) {

        return crudOperator.getGarage(garageCode, resultProcessor);
    }

    public <T> T addGarage(Garage garage, ServiceResultProcessor<Garage, T> resultProcessor) {

        return crudOperator.addGarage(garage, resultProcessor);
    }

    public <T> T updateGarage(Garage garage, ServiceResultProcessor<Garage, T> resultProcessor) {

        return crudOperator.updateGarage(garage, resultProcessor);
    }

    public <T> T removeGarage(String garageCode, ResultProcessor<T> resultProcessor) {

        return crudOperator.removeGarage(garageCode, resultProcessor);
    }
}
