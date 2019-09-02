package com.cognizant.icecream.components;


import com.cognizant.icecream.clients.TruckCRUD;
import com.cognizant.icecream.components.api.TruckCRUDOperator;
import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.pools.api.ResultPool;
import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class TruckCRUDOperatorBean implements TruckCRUDOperator {

    private static final String VERIFY_ADD = "Verify that it was added and not subsequently removed.";
    private static final String NOT_FOUND = "Could not find truck of VIN %s. " + VERIFY_ADD;
    private static final String COULD_NOT_ADD = "Could not add truck of VIN %s.";
    private static final String COULD_NOT_UPDATE = "Could not update truck of VIN %s. " + VERIFY_ADD;

    private static final Result REMOVED;

    private TruckCRUD truckCRUD;
    private ResultPool resultPool;
    private ServiceResultPool<Truck> serviceResultPool;

    static {
        REMOVED = ResultFactory.createResult(true, "REMOVED");
    }

    @Autowired
    TruckCRUDOperatorBean(TruckCRUD truckCRUD, ResultPool resultPool, ServiceResultPool<Truck> serviceResultPool) {

        this.truckCRUD = truckCRUD;
        this.resultPool = resultPool;
        this.serviceResultPool = serviceResultPool;
    }

    @Override
    public <T> T getTruck(String vin, ServiceResultProcessor<Truck, T> resultProcessor) {

        Optional<Truck> truck = truckCRUD.findByVIN(vin);
        return ComponentsUtil.processOptional(truck, NOT_FOUND, vin, serviceResultPool, resultProcessor);
    }

    @Override
    public <T> T addTruck(Truck truck, ServiceResultProcessor<Truck, T> resultProcessor) {

        Optional<Truck> added = truckCRUD.add(truck);
        return ComponentsUtil.processOptional(added, COULD_NOT_ADD, truck.getVin(), serviceResultPool, resultProcessor);
    }

    @Override
    public <T> T updateTruck(Truck truck, ServiceResultProcessor<Truck, T> resultProcessor) {

        Optional<Truck> updated = truckCRUD.update(truck);
        return ComponentsUtil.processOptional(updated, COULD_NOT_UPDATE, truck.getVin(), serviceResultPool, resultProcessor);
    }

    @Override
    public <T> T removeTruck(String vin, ResultProcessor<T> resultProcessor) {

        boolean success = truckCRUD.remove(vin);

        if(success) {
            return resultProcessor.apply(REMOVED);
        }

        MutableResult result = ComponentsUtil.createResult(false, "Could not remove Truck: " + vin, resultPool);

        return ComponentsUtil.processResult(result, resultProcessor, resultPool);
    }

    @Override
    public boolean exists(Truck truck) {

        Optional optional = truckCRUD.findByVIN(truck.getVin());
        return optional.isPresent();
    }

    @Override
    public boolean exists(String authorization, Truck truck) {

        Optional optional = truckCRUD.findByVIN(authorization, truck.getVin());
        return optional.isPresent();
    }
}
