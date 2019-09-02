package com.cognizant.icecream.components;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.TruckDeploymentClient;
import com.cognizant.icecream.models.Neighborhood;
import com.cognizant.icecream.models.TruckGarage;
import com.cognizant.icecream.pools.api.ResultPool;
import com.cognizant.icecream.result.MutableResult;
import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.result.ResultProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TruckDeployer {

    private static final String NONEXISTENT_TRUCK = "Could not find truck with VIN %s.";
    private static final String COULD_NOT_PATROL = "Could not order patrol. Verify that this patrol is not in progress.";
    private static final String ALREADY_DEPLOYED = "Truck of VIN %s has already been deployed to Garage %s";
    private static final String INVALID_GARAGE = "Garage with code %s does not exist.";

    private GarageCRUD garageCRUD;
    private GarageCache garageCache;
    private TruckCRUDOperator crudOperator;
    private TruckDeploymentClient client;
    private ResultPool pool;

    @Autowired
    public TruckDeployer(
            GarageCRUD garageCRUD,
            GarageCache garageCache,
            TruckCRUDOperator crudOperator,
            TruckDeploymentClient client,
            ResultPool pool
    ) {
        this.garageCRUD = garageCRUD;
        this.garageCache = garageCache;
        this.crudOperator = crudOperator;
        this.client = client;
        this.pool = pool;
    }

    public <T> T deploy(String authorization, TruckGarage truckGarage, ResultProcessor<T> resultProcessor) {

        if(!crudOperator.exists(authorization, truckGarage.getTruck())) {

            String errMsg = String.format(NONEXISTENT_TRUCK, truckGarage.getTruck().getVin());
            return ComponentsUtil.processResult(errMsg, resultProcessor, pool);
        }

        if(!garageCache.validate(authorization, truckGarage.getGarage())) {

            String errMsg = String.format(INVALID_GARAGE, truckGarage.getGarage().getCode());
            return ComponentsUtil.processResult(errMsg, resultProcessor, pool);
        }

        if(alreadyDeployed(authorization, truckGarage)) {
            String errMsg = String.format(ALREADY_DEPLOYED, truckGarage.getTruck().getVin(), truckGarage.getGarage().getCode());
            MutableResult result = ComponentsUtil.createResult(false, errMsg, pool);

            return ComponentsUtil.processResult(result, resultProcessor, pool);
        }

        Result result = client.deployTruck(authorization, truckGarage);

        return resultProcessor.apply(result);
    }

    public <T> T patrol(boolean alcoholic, Neighborhood neighborhood, ResultProcessor<T> resultProcessor) {

        boolean success = client.patrol(alcoholic, neighborhood);

        MutableResult result;

        if(success) {
            result = ComponentsUtil.createResult(true, "success", pool);
        }
        else {
            result = ComponentsUtil.createResult(false, COULD_NOT_PATROL, pool);
        }

        return ComponentsUtil.processResult(result, resultProcessor, pool);
    }

    private boolean alreadyDeployed(String authorization, TruckGarage truckGarage) {

        Optional<TruckGarage> current = garageCRUD.findTruckGarageByTruck(authorization, truckGarage.getTruck());

        if(!current.isPresent()) {
            return false;
        }

        return current.get().equals(truckGarage);
    }
}
