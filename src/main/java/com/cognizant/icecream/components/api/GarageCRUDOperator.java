package com.cognizant.icecream.components.api;

import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.result.ResultProcessor;
import com.cognizant.icecream.result.ServiceResultProcessor;

public interface GarageCRUDOperator {

    <T> T getGarage(String garageCode, ServiceResultProcessor<Garage, T> resultProcessor);
    <T> T addGarage(Garage garage, ServiceResultProcessor<Garage, T> resultProcessor);
    <T> T updateGarage(Garage garage, ServiceResultProcessor<Garage, T> resultProcessor);
    <T> T removeGarage(String garageCode, ResultProcessor<T> resultProcessor);
}
