package com.cognizant.icecream.components.api;

import com.cognizant.icecream.models.Garage;

public interface GarageCache {

    Garage getGarage(String authorization, String garageCode);
    boolean validate(String authorization, Garage garage);
}
