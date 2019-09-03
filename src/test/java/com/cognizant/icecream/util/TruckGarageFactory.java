package com.cognizant.icecream.util;

import com.cognizant.icecream.api.models.Garage;
import com.cognizant.icecream.api.models.Truck;
import com.cognizant.icecream.api.models.TruckGarage;

public class TruckGarageFactory {

    public static TruckGarage createTruckGarage(Garage garage, Truck truck) {

        TruckGarage truckGarage = new TruckGarage();
        truckGarage.setGarage(garage);
        truckGarage.setTruck(truck);

        return truckGarage;
    }
}
