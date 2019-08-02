package com.cognizant.icecream.services;

import com.cognizant.icecream.models.*;
import com.cognizant.icecream.services.api.Result;
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

        return new Result();
    }

    public Invoice purchaseTrucks(String authorization, TruckPurchaseOrder order) {

        return new Invoice();
    }

    public Result deploy(TruckGarage truckGarage) {

        return new Result();
    }

    public Result undeploy(TruckGarage truckGarage) {

        return new Result();
    }

    public Result patrol(boolean alcoholic, Neighborhood neighborhood) {

        return new Result();
    }

    public Set<Truck> getTrucks() {

        return new HashSet<>();
    }

    public Set<Truck> getTrucks(String garageCode) {

        return new HashSet<>();
    }

    public Set<Truck> getTrucks(boolean alcholic) {

        return new HashSet<>();
    }

    public Set<Truck> getTrucks(String garageCode, boolean alcoholic) {

        return new HashSet<>();
    }

}
