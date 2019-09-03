package com.cognizant.icecream.components;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.TruckCRUD;
import com.cognizant.icecream.components.api.GarageCache;
import com.cognizant.icecream.components.api.TrucksFilter;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.models.TruckGarage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
class TrucksFilterBean implements TrucksFilter {

    private static final Set<Truck> EMPTY_SET = new HashSet<>();

    private TruckCRUD truckCRUD;
    private GarageCRUD garageCRUD;
    private GarageCache garageCache;

    private String authorization;

    @Autowired
    TrucksFilterBean(TruckCRUD truckCRUD, GarageCRUD garageCRUD, GarageCache garageCache, HttpServletRequest request) {

        this.truckCRUD = truckCRUD;
        this.garageCRUD = garageCRUD;
        this.garageCache = garageCache;

        this.authorization = request.getHeader("Authorization");
    }

    @Override
    public Set<Truck> getTrucks() {

        return truckCRUD.findAll();
    }

    @Override
    public Set<Truck> getTrucks(String garageCode) {

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
    public Set<Truck> getTrucks(String garageCode, boolean alcoholic) {

        Set<Truck> trucks = getTrucks(garageCode);

        trucks.removeIf(t -> t.isAlcoholic() ^ alcoholic);

        return trucks;
    }

    private Set<Truck> toTruckSet(Set<TruckGarage> garageTrucks) {

        Set<Truck> truckSet = new HashSet<>();

        garageTrucks.forEach(gt -> truckSet.add(gt.getTruck()));

        return truckSet;
    }
}
