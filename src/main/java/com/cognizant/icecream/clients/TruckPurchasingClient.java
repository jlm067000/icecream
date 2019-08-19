package com.cognizant.icecream.clients;

import com.cognizant.icecream.clients.result.ClientResultFactory;
import com.cognizant.icecream.models.result.ClientResult;
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

    public ClientResult purchaseTrucks(TruckPurchaseOrder tpo) {

        Garage garage = tpo.getGarage();

        boolean garageExists = garageCRUD.findByCode(garage.getCode()).isPresent();

        if(!garageExists) {
            return ClientResultFactory.createResult(false, "Garage " + garage.getCode() + " does not exist");
        }

        Optional<Truck> preexisting = preexistingTruck(tpo.getTrucks());

        if(preexisting.isPresent()) {

            String vin = preexisting.get().getVin();
            String errMsg = "Truck of VIN " + vin + " has already been purchased";

            return ClientResultFactory.createResult(false, errMsg);
        }

        try {
            Thread.sleep(2000);
        }
        catch(InterruptedException ex) {}

        return ClientResultFactory.createResult(true, "Purchase scheduled");
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
