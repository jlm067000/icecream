package com.cognizant.icecream.services;

import com.cognizant.icecream.models.*;
import com.cognizant.icecream.clients.ResultObject;
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

    public ResultObject removeTruck(String vin) {

        return new ResultObject();
    }

    public Invoice purchaseTrucks(String authorization, TruckPurchaseOrder order) {

        return new Invoice();
    }

    public ResultObject deploy(TruckGarage truckGarage) {

        return new ResultObject();
    }

    public ResultObject undeploy(TruckGarage truckGarage) {

        return new ResultObject();
    }

    public ResultObject patrol(boolean alcoholic, Neighborhood neighborhood) {

        return new ResultObject();
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
