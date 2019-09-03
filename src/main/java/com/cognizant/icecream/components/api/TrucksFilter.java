package com.cognizant.icecream.components.api;

import com.cognizant.icecream.models.Truck;

import java.util.Set;

public interface TrucksFilter {

    Set<Truck> getTrucks();
    Set<Truck> getTrucks(String garageCode);
    Set<Truck> getTrucks(boolean alcoholic);
    Set<Truck> getTrucks(String garageCode, boolean alcoholic);
}
