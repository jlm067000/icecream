package com.cognizant.icecream.components.api;

import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.result.ResultProcessor;
import com.cognizant.icecream.result.ServiceResultProcessor;

public interface TruckCRUDOperator {

    <T> T getTruck(String vin, ServiceResultProcessor<Truck, T> resultProcessor);
    <T> T addTruck(Truck truck, ServiceResultProcessor<Truck, T> resultProcessor);
    <T> T updateTruck(Truck truck, ServiceResultProcessor<Truck, T> resultProcessor);
    <T> T removeTruck(String vin, ResultProcessor<T> resultProcessor);
    boolean exists(Truck truck);
    boolean exists(String authorization, Truck truck);
}
