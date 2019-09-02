package com.cognizant.icecream.components;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.models.Garage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class GarageCache {

    private GarageCRUD crud;
    private Map<String, Garage> cache;

    @Autowired
    public GarageCache(GarageCRUD crud) {

        this.crud = crud;
        this.cache = new HashMap<>();
    }

    public boolean validate(String authorization, Garage garage) {

        String garageCode = garage.getCode();

        if(cache.containsKey(garageCode)) {
            return true;
        }

        Optional<Garage> optional = crud.findByCode(authorization, garageCode);

        if(!optional.isPresent()) {
            return false;
        }

        cache.put(garageCode, optional.get());

        return true;
    }
}
