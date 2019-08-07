package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.Result;
import com.cognizant.icecream.clients.ResultFactory;
import com.cognizant.icecream.models.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TruckService {

    public Truck getTruck(String vin) {

        return new Truck();
    }

    public Truck addTruck(Truck truck) {

        return truck;
    }

    public Truck updateTruck(Truck truck) {

        return truck;
    }

    public Result removeTruck(String vin) {

        return ResultFactory.createResult(true, "removed");
    }

    public Invoice purchaseTrucks(String authorization, TruckPurchaseOrder order) {

        return new Invoice();
    }

    public Result deploy(TruckGarage truckGarage) {

        return ResultFactory.createResult(true, "deployed");
    }

    public Result patrol(boolean alcoholic, Neighborhood neighborhood) {

        return ResultFactory.createResult(true, "patrol");
    }

    public Set<Truck> getTrucks() {

        return new HashSet<>();
    }

    public Set<Truck> getTrucks(String garageCode) {

        return new HashSet<>();
    }

    public Set<Truck> getTrucks(boolean alcoholic) {

        return new HashSet<>();
    }

    public Set<Truck> getTrucks(String garageCode, boolean alcoholic) {

        return new HashSet<>();
    }
}
