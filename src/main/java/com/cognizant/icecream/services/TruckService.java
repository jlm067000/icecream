package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.*;
import com.cognizant.icecream.models.*;
import com.cognizant.icecream.models.result.ClientResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TruckService {

    private static final ClientResult removed;

    private GarageCRUD garageCRUD;
    private TruckCRUD truckCRUD;
    private TruckPurchasingClient purchasingClient;

    static {
        removed = ClientResultFactory.createResult(true, "removed");
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

    public ClientResult removeTruck(String vin) {

        boolean success = truckCRUD.remove(vin);

        if(success) {
            return removed;
        }
        else {
            return ClientResultFactory.createResult(false, "Could not remove Truck: " + vin);
        }
    }

    public Invoice purchaseTrucks(String authorization, TruckPurchaseOrder order) {

        return new Invoice();
    }

    public ClientResult deploy(TruckGarage truckGarage) {

        return ClientResultFactory.createResult(true, "deployed");
    }

    public ClientResult patrol(boolean alcoholic, Neighborhood neighborhood) {

        return ClientResultFactory.createResult(true, "patrol");
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
