package com.cognizant.icecream.components;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.TruckCRUD;
import com.cognizant.icecream.components.api.GarageCache;
import com.cognizant.icecream.components.api.TrucksFilter;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.models.TruckGarage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
class TrucksFilterBean implements TrucksFilter {

    private static final Set<Truck> EMPTY_SET = new HashSet<>();

    private TruckCRUD truckCRUD;
    private GarageCRUD garageCRUD;
    private GarageCache garageCache;

    @Autowired
    TrucksFilterBean(TruckCRUD truckCRUD, GarageCRUD garageCRUD, GarageCache garageCache) {

        this.truckCRUD = truckCRUD;
        this.garageCRUD = garageCRUD;
        this.garageCache = garageCache;
    }

    @Override
    public Set<Truck> getTrucks() {

        return truckCRUD.findAll();
    }

    @Override
    public Set<Truck> getTrucks(String authorization, String garageCode) {

        Garage garage = garageCache.getGarage(authorization, garageCode);

        if(garage == null) {
            return EMPTY_SET;
        }

        Set<TruckGarage> garageTrucks = garageCRUD.findAllByGarage(authorization, garage);

        return toTruckSet(garageTrucks);
    }

    @Override
    public Set<Truck> getTrucks(boolean alcoholic) {

        Set<Truck> trucks = truckCRUD.findAll();

        trucks.removeIf(t -> t.isAlcoholic() ^ alcoholic);

        return trucks;
    }

    @Override
    public Set<Truck> getTrucks(String authorization, String garageCode, boolean alcoholic) {

        Set<Truck> trucks = getTrucks(authorization, garageCode);

        trucks.removeIf(t -> t.isAlcoholic() ^ alcoholic);

        return trucks;
    }

    private Set<Truck> toTruckSet(Set<TruckGarage> garageTrucks) {

        Set<Truck> truckSet = new HashSet<>();

        garageTrucks.forEach(gt -> truckSet.add(gt.getTruck()));

        return truckSet;
    }
}
