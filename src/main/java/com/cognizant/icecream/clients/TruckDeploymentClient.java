package com.cognizant.icecream.clients;

import com.cognizant.icecream.result.ResultFactory;
import com.cognizant.icecream.result.Result;
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


    public Result deployTruck(TruckGarage truckGarage) {

        if(alreadyDeployed(truckGarage)) {

            String errMsg = "Truck has already been deployed to Garage " + truckGarage.getGarage().getCode();

            return ResultFactory.createResult(false, errMsg);
        }

        try {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex) {}

        return ResultFactory.createResult(true, "Deployment scheduled");
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
