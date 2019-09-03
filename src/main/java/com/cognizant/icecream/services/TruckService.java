package com.cognizant.icecream.services;

import com.cognizant.icecream.api.models.*;
import com.cognizant.icecream.api.result.ResultProcessor;
import com.cognizant.icecream.api.result.ServiceResultProcessor;
import com.cognizant.icecream.components.api.TruckCRUDOperator;
import com.cognizant.icecream.components.api.TruckDeployer;
import com.cognizant.icecream.components.api.TruckPurchaser;
import com.cognizant.icecream.components.api.TrucksFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TruckService {

    private TrucksFilter filter;
    private TruckCRUDOperator crudOperator;
    private TruckDeployer deployer;
    private TruckPurchaser purchaser;

    @Autowired
    public TruckService(
            TruckCRUDOperator crudOperator,
            TrucksFilter filter,
            TruckPurchaser purchaser,
            TruckDeployer deployer
    ) {
        this.filter = filter;
        this.crudOperator = crudOperator;
        this.deployer = deployer;
        this.purchaser = purchaser;
    }

    public <T> T getTruck(String vin, ServiceResultProcessor<Truck, T> resultProcessor) {

        return crudOperator.getTruck(vin, resultProcessor);
    }

    public <T> T addTruck(Truck truck, ServiceResultProcessor<Truck, T> resultProcessor) {

        return crudOperator.addTruck(truck, resultProcessor);
    }

    public <T> T updateTruck(Truck truck, ServiceResultProcessor<Truck, T> resultProcessor) {

        return crudOperator.updateTruck(truck, resultProcessor);
    }

    public <T> T removeTruck(String vin, ResultProcessor<T> resultProcessor) {

        return crudOperator.removeTruck(vin, resultProcessor);
    }

    public <T> T purchaseTrucks(
            String authorization,
            TruckPurchaseOrder order,
            ServiceResultProcessor<Invoice, T> resultProcessor
    ) {

        return purchaser.purchaseTrucks(authorization, order, resultProcessor);
    }

    public <T> T deploy(String authorization, TruckGarage truckGarage, ResultProcessor<T> resultProcessor) {

        return deployer.deploy(authorization, truckGarage, resultProcessor);
    }

    public <T> T patrol(boolean alcoholic, Neighborhood neighborhood, ResultProcessor<T> resultProcessor) {

        return deployer.patrol(alcoholic, neighborhood, resultProcessor);
    }

    public Set<Truck> getTrucks() {

        return filter.getTrucks();
    }

    public Set<Truck> getTrucks(String authorization, String garageCode) {

        return filter.getTrucks(authorization, garageCode);
    }

    public Set<Truck> getTrucks(boolean alcoholic) {

        return filter.getTrucks(alcoholic);
    }

    public Set<Truck> getTrucks(String authorization, String garageCode, boolean alcoholic) {

        return filter.getTrucks(authorization, garageCode, alcoholic);
    }
}
