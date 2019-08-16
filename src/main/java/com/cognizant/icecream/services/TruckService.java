package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.*;
import com.cognizant.icecream.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TruckService {

    private static final Result removed;

    private GarageCRUD garageCRUD;
    private TruckCRUD truckCRUD;
    private TruckPurchasingClient purchasingClient;

    static {
        removed = ResultFactory.createResult(true, "removed");
    }

    @Autowired
    public TruckService(GarageCRUD garageCRUD, TruckCRUD truckCRUD, TruckPurchasingClient purchasingClient) {

        this.garageCRUD = garageCRUD;
        this.truckCRUD = truckCRUD;
        this.purchasingClient = purchasingClient;
    }

    public Truck getTruck(String vin) {

        return ServicesUtil.extractOptionally(vin, truckCRUD::findByVIN);
    }

    public Truck addTruck(Truck truck) {

        return ServicesUtil.extractOptionally(truck, truckCRUD::add);
    }

    public Truck updateTruck(Truck truck) {

        return ServicesUtil.extractOptionally(truck, truckCRUD::update);
    }

    public Result removeTruck(String vin) {

        boolean success = truckCRUD.remove(vin);

        if(success) {
            return removed;
        }
        else {
            return ResultFactory.createResult(false, "Could not remove Truck: " + vin);
        }
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
