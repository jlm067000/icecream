package com.cognizant.icecream.util;

import com.cognizant.icecream.models.Truck;

public class TruckFactory {

    public static Truck createTruck(boolean alcoholic, String vin) {

        Truck truck = new Truck();
        truck.setVin(vin);
        truck.setAlcoholic(alcoholic);

        return truck;
    }
}
