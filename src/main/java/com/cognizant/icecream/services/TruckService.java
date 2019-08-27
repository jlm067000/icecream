package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.TruckCRUD;
import com.cognizant.icecream.clients.TruckPurchasingClient;
import com.cognizant.icecream.models.*;
import com.cognizant.icecream.pools.api.ResultPool;
import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TruckService {

    private static final String VERIFY_ADD = "Verify that it was added and not REMOVED.";
    private static final String NOT_FOUND = "Could not find truck of VIN %s. " + VERIFY_ADD;
    private static final String COULD_NOT_ADD = "Could not add truck of VIN %s.";
    private static final String COULD_NOT_UPDATE = "Could not update truck of VIN %s. " + VERIFY_ADD;

    private static final Result REMOVED;

    private GarageCRUD garageCRUD;
    private TruckCRUD truckCRUD;
    private TruckPurchasingClient purchasingClient;
    private ResultPool resultPool;
    private ServiceResultPool<Truck> serviceResultPool;

    static {
        REMOVED = ResultFactory.createResult(true, "REMOVED");
    }

    @Autowired
    public TruckService(
            GarageCRUD garageCRUD,
            TruckCRUD truckCRUD,
            TruckPurchasingClient purchasingClient,
            ResultPool resultPool,
            ServiceResultPool<Truck> serviceResultPool
    ) {

        this.garageCRUD = garageCRUD;
        this.truckCRUD = truckCRUD;
        this.purchasingClient = purchasingClient;
        this.resultPool = resultPool;
        this.serviceResultPool = serviceResultPool;
    }

    public <T> T getTruck(String vin, ServiceResultProcessor<Truck, T> resultProcessor) {

        Optional<Truck> truck = truckCRUD.findByVIN(vin);

        return ServicesUtil.processOptional(truck, NOT_FOUND, vin, serviceResultPool, resultProcessor);
    }

    public <T> T addTruck(Truck truck, ServiceResultProcessor<Truck, T> resultProcessor) {

        Optional<Truck> added = truckCRUD.add(truck);
        return ServicesUtil.processOptional(added, COULD_NOT_ADD, truck.getVin(), serviceResultPool, resultProcessor);
    }

    public <T> T updateTruck(Truck truck, ServiceResultProcessor<Truck, T> resultProcessor) {

        Optional<Truck> updated = truckCRUD.update(truck);
        return ServicesUtil.processOptional(updated, COULD_NOT_UPDATE, truck.getVin(), serviceResultPool, resultProcessor);
    }

    public <T> T removeTruck(String vin, ResultProcessor<T> resultProcessor) {

        boolean success = truckCRUD.remove(vin);

        if(success) {
            return resultProcessor.apply(REMOVED);
        }

        Result result = ServicesUtil.createResult(false, "Could not remove Truck: " + vin, resultPool);

        return resultProcessor.apply(result);
    }

    public Invoice purchaseTrucks(String authorization, TruckPurchaseOrder order) {

        return new Invoice();
    }

    public Result deploy(TruckGarage truckGarage) {

        return ResultFactory.createResult(true, "deployed");
    }

    public Result patrol(boolean alcoholic, Neighborhood neighborhood) {

        return ResultFactory.createResult(true, "patrol");
    }

    public <T> T getTrucks(ServiceResultProcessor<Set<Truck>, T> resultProcessor) {

        Set<Truck> trucks = new HashSet<>();
        ServiceResult<Set<Truck>> result = ResultFactory.createServiceResult(true, "", trucks);
        return resultProcessor.apply(result);
    }

    public Set<Truck> getTrucks(String garageCode) {

        return new HashSet<>();
    }

    public Set<Truck> getTrucks(boolean alcoholic) {

        return new HashSet<>();
    }

    public Set<Truck> getTrucks(String garageCode, boolean alcoholic) {

        return new HashSet<>();
    }
}
