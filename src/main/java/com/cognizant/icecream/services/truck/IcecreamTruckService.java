package com.cognizant.icecream.services.truck;

import com.cognizant.icecream.models.Invoice;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TruckGarage;
import com.cognizant.icecream.models.TruckPurchaseOrder;
import com.cognizant.icecream.services.truck.contract.Result;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class IcecreamTruckService {


    public Invoice purchaseTrucks(String authorization, TruckPurchaseOrder order) {

        return new Invoice();
    }

    public Set<Garage> getLocations() {
        return new HashSet<>();
    }

    public Result deploy(TruckGarage truckLocation) {

        return new Result();
    }

}
