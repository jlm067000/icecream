package com.cognizant.icecream.clients;

import com.cognizant.icecream.result.ResultFactory;
import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.models.TruckPurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;


@Component
public class TruckPurchasingClient {

    @Autowired
    private GarageCRUD garageCRUD;

    @Autowired
    private TruckCRUD truckCRUD;

    @Autowired
    public TruckPurchasingClient(GarageCRUD garageCRUD, TruckCRUD truckCRUD) {

        this.garageCRUD = garageCRUD;
        this.truckCRUD = truckCRUD;
    }

    public Result purchaseTrucks(TruckPurchaseOrder tpo) {

        Garage garage = tpo.getGarage();

        boolean garageExists = garageCRUD.findByCode(garage.getCode()).isPresent();

        if(!garageExists) {
            return ResultFactory.createResult(false, "Garage " + garage.getCode() + " does not exist");
        }

        Optional<Truck> preexisting = preexistingTruck(tpo.getTrucks());

        if(preexisting.isPresent()) {

            String vin = preexisting.get().getVin();
            String errMsg = "Truck of VIN " + vin + " has already been purchased";

            return ResultFactory.createResult(false, errMsg);
        }

        try {
            Thread.sleep(2000);
        }
        catch(InterruptedException ex) {}

        return ResultFactory.createResult(true, "Purchase scheduled");
    }

    private Optional<Truck> preexistingTruck(Set<Truck> trucks) {

        Set<Truck> existing = truckCRUD.findAll();

        for(Truck truck : trucks) {

            if(existing.contains(truck)) {
                return Optional.of(truck);
            }
        }

        return Optional.empty();
    }
}
