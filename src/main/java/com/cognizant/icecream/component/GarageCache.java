package com.cognizant.icecream.component;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.models.Garage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class GarageCache {

    private Map<String, Garage> garageCache;
    private GarageCRUD garageCRUD;

    @Autowired
    public GarageCache(GarageCRUD garageCRUD) {
        this.garageCache = new HashMap<>();
        this.garageCRUD = garageCRUD;
    }

    public Garage getGarage(String authorization, String garageCode) {

        if(garageCache.containsKey(garageCode)) {
            return garageCache.get(garageCode);
        }

        Optional<Garage> garage = garageCRUD.findByCode(authorization, garageCode);

        if(!garage.isPresent()) {
            return null;
        }

        garageCache.put(garageCode, garage.get());

        return garage.get();
    }

    public boolean validate(String authorization, Garage garage) {

        String garageCode = garage.getCode();

        if(garageCache.containsKey(garageCode)) {
            return true;
        }

        Optional<Garage> optional = garageCRUD.findByCode(authorization, garageCode);

        if(!optional.isPresent()) {
            return false;
        }

        garageCache.put(garageCode, optional.get());

        return true;
    }
}
