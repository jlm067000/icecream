package com.cognizant.icecream.services.truck;

import com.cognizant.icecream.models.*;
import com.cognizant.icecream.services.truck.contract.Result;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class IcecreamTruckService {


    public Invoice purchaseTrucks(String authorization, TruckPurchaseOrder order) {

        return new Invoice();
    }

    public Result deploy(TruckGarage truckGarage) {

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

    public Result resupply(String garageCode, TimeSlot timeSlot) {

        return new Result();
    }

}
