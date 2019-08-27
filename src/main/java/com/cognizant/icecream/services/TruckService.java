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

import java.util.*;

@Service
public class TruckService {

    private static final String VERIFY_ADD = "Verify that it was added and not REMOVED.";
    private static final String NOT_FOUND = "Could not find truck of VIN %s. " + VERIFY_ADD;
    private static final String COULD_NOT_ADD = "Could not add truck of VIN %s.";
    private static final String COULD_NOT_UPDATE = "Could not update truck of VIN %s. " + VERIFY_ADD;
    private static final String COULD_NOT_PURCHASE = "Failed to process Purchase Order. " +
            "                       Contact Accounting Department for more information";

    private static final Result REMOVED;

    private static final Set<Truck> EMPTY_SET;

    private GarageCRUD garageCRUD;
    private TruckCRUD truckCRUD;
    private TruckPurchasingClient purchasingClient;
    private ResultPool resultPool;
    private ServiceResultPool<Truck> serviceResultPool;
    private ServiceResultPool<Invoice> invoiceResultPool;

    private Map<String, Garage> garageCache;

    static {
        REMOVED = ResultFactory.createResult(true, "REMOVED");
        EMPTY_SET = new HashSet<>();
    }

    @Autowired
    public TruckService(
            GarageCRUD garageCRUD,
            TruckCRUD truckCRUD,
            TruckPurchasingClient purchasingClient,
            ResultPool resultPool,
            ServiceResultPool<Truck> serviceResultPool,
            ServiceResultPool<Invoice> invoiceResultPool
    ) {

        this.garageCRUD = garageCRUD;
        this.truckCRUD = truckCRUD;
        this.purchasingClient = purchasingClient;
        this.resultPool = resultPool;
        this.serviceResultPool = serviceResultPool;
        this.invoiceResultPool = invoiceResultPool;

        this.garageCache = new HashMap<>();
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

    public <T> T purchaseTrucks(
            String authorization,
            TruckPurchaseOrder order,
            ServiceResultProcessor<Invoice, T> resultProcessor)
    {

        Optional<Invoice> invoice = purchasingClient.purchaseTrucks(order);

        return ServicesUtil.processOptional(invoice, COULD_NOT_PURCHASE, invoiceResultPool, resultProcessor);
    }

    public Result deploy(TruckGarage truckGarage) {

        return ResultFactory.createResult(true, "deployed");
    }

    public Result patrol(boolean alcoholic, Neighborhood neighborhood) {

        return ResultFactory.createResult(true, "patrol");
    }

    public Set<Truck> getTrucks() {

        return truckCRUD.findAll();
    }

    public Set<Truck> getTrucks(String garageCode) {

        Garage garage = getGarage(garageCode);

        if(garage == null) {
            return EMPTY_SET;
        }

        Set<TruckGarage> garageTrucks = garageCRUD.findAllByGarage(garage);

        return toTruckSet(garageTrucks);
    }

    public Set<Truck> getTrucks(boolean alcoholic) {

        Set<Truck> trucks = truckCRUD.findAll();

        trucks.removeIf(t -> t.isAlcoholic() ^ alcoholic);

        return trucks;
    }

    public Set<Truck> getTrucks(String garageCode, boolean alcoholic) {

        Set<Truck> trucks = getTrucks(garageCode);

        trucks.removeIf(t -> t.isAlcoholic() ^ alcoholic);

        return trucks;
    }

    private Garage getGarage(String garageCode) {

        if(garageCache.containsKey(garageCode)) {
            return garageCache.get(garageCode);
        }

        Optional<Garage> garage = garageCRUD.findByCode(garageCode);

        if(!garage.isPresent()) {
            return null;
        }

        garageCache.put(garageCode, garage.get());
        return garage.get();
    }

    private Set<Truck> toTruckSet(Set<TruckGarage> garageTrucks) {

        Set<Truck> truckSet = new HashSet<>();

        garageTrucks.forEach(gt -> truckSet.add(gt.getTruck()));

        return truckSet;
    }
}
