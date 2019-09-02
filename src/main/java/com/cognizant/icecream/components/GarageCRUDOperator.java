package com.cognizant.icecream.components;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.pools.api.ResultPool;
import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GarageCRUDOperator {

    private static final Result REMOVED;
    private static final String NOT_FOUND = "Garage %s not found.";
    private static final String COULD_NOT_ADD = "Could not add Garage %s.";
    private static final String COULD_NOT_UPDATE = "Could not update Garage %s. Verify that this Garage has been added.";

    private GarageCRUD garageCRUD;
    private ResultPool resultPool;
    private ServiceResultPool<Garage> serviceResultPool;

    static {
        REMOVED = ResultFactory.createResult(true, "REMOVED");
    }

    @Autowired
    public GarageCRUDOperator(GarageCRUD garageCRUD, ResultPool resultPool, ServiceResultPool<Garage> serviceResultPool) {
        this.garageCRUD = garageCRUD;
        this.resultPool = resultPool;
        this.serviceResultPool = serviceResultPool;
    }

    public <T> T getGarage(String garageCode, ServiceResultProcessor<Garage, T> resultProcessor) {

        Optional<Garage> garage = garageCRUD.findByCode(garageCode);

        return ComponentsUtil.processOptional(garage, NOT_FOUND, garageCode, serviceResultPool, resultProcessor);
    }

    public <T> T addGarage(Garage garage, ServiceResultProcessor<Garage, T> resultProcessor) {

        Optional<Garage> added = garageCRUD.add(garage);

        return ComponentsUtil.processOptional(added, COULD_NOT_ADD, garage.getCode(), serviceResultPool, resultProcessor);
    }

    public <T> T updateGarage(Garage garage, ServiceResultProcessor<Garage, T> resultProcessor) {

        Optional<Garage> updated = garageCRUD.update(garage);

        return ComponentsUtil.processOptional(updated, COULD_NOT_UPDATE, garage.getCode(), serviceResultPool, resultProcessor);
    }

    public <T> T removeGarage(String garageCode, ResultProcessor<T> resultProcessor) {

        boolean success = garageCRUD.remove(garageCode);

        if(success) {
            return resultProcessor.apply(REMOVED);
        }

        MutableResult result =
                ComponentsUtil.createResult(false, "Could not remove Garage: " + garageCode, resultPool);

        return ComponentsUtil.processResult(result, resultProcessor, resultPool);
    }
}
