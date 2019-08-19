package com.cognizant.icecream.clients;

import com.cognizant.icecream.clients.result.ClientResultFactory;
import com.cognizant.icecream.models.result.ClientResult;
import com.cognizant.icecream.models.Neighborhood;
import com.cognizant.icecream.models.TruckGarage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TruckDeploymentClient {

    private GarageCRUD garageCRUD;

    @Autowired
    public TruckDeploymentClient(GarageCRUD garageCRUD) {
        this.garageCRUD = garageCRUD;
    }


    public ClientResult deployTruck(TruckGarage truckGarage) {

        if(alreadyDeployed(truckGarage)) {

            String errMsg = "Truck has already been deployed to Garage " + truckGarage.getGarage().getCode();

            return ClientResultFactory.createResult(false, errMsg);
        }

        try {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex) {}

        return ClientResultFactory.createResult(true, "Deployment scheduled");
    }

    private boolean alreadyDeployed(TruckGarage truckGarage) {

        Set<TruckGarage> deployed = garageCRUD.findAllByGarage(truckGarage.getGarage());

        return deployed.contains(truckGarage);
    }

    public boolean patrol(boolean isAlcoholic, Neighborhood neighborhood) {

        try {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex) {
            return false;
        }

        return true;
    }

}
