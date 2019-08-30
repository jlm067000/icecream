package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.TruckCRUD;
import com.cognizant.icecream.clients.TruckDeploymentClient;
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

    private static final String VERIFY_ADD = "Verify that it was added and not subsequently removed.";
    private static final String NOT_FOUND = "Could not find truck of VIN %s. " + VERIFY_ADD;
    private static final String COULD_NOT_ADD = "Could not add truck of VIN %s.";
    private static final String COULD_NOT_UPDATE = "Could not update truck of VIN %s. " + VERIFY_ADD;
    private static final String COULD_NOT_PURCHASE = "Failed to process Purchase Order. " +
            "                       Contact Accounting Department for more information.";
    private static final String PREEXISTING_TRUCK = "Could not process Purchase Order because truck of VIN %s " +
                                    "has already been purchased.";
    private static final String INVALID_GARAGE = "Garage with code %s does not exist.";
    private static final String NONEXISTENT_TRUCK = "Could not find truck with VIN %s.";
    private static final String COULD_NOT_PATROL = "Could not order patrol. Verify that this patrol is not in progress.";
    private static final String ALREADY_DEPLOYED = "Truck of VIN %s has already been deployed to Garage %s";

    private static final Result REMOVED;

    private static final Set<Truck> EMPTY_SET;

    private GarageCRUD garageCRUD;
    private TruckCRUD truckCRUD;
    private TruckPurchasingClient purchasingClient;
    private TruckDeploymentClient deploymentClient;
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
            TruckDeploymentClient deploymentClient,
            ResultPool resultPool,
            ServiceResultPool<Truck> serviceResultPool,
            ServiceResultPool<Invoice> invoiceResultPool
    ) {

        this.garageCRUD = garageCRUD;
        this.truckCRUD = truckCRUD;
        this.purchasingClient = purchasingClient;
        this.deploymentClient = deploymentClient;
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

        MutableResult result = ServicesUtil.createResult(false, "Could not remove Truck: " + vin, resultPool);

        return ServicesUtil.processResult(result, resultProcessor, resultPool);
    }

    public <T> T purchaseTrucks(
            String authorization,
            TruckPurchaseOrder order,
            ServiceResultProcessor<Invoice, T> resultProcessor
    ) {

        Truck preexisting = findPreexisting(order.getTrucks());

        if(preexisting != null) {
            return processPurchaseError(PREEXISTING_TRUCK, preexisting.getVin(), resultProcessor);
        }

        if(!validate(order.getGarage())) {
            return processPurchaseError(INVALID_GARAGE, order.getGarage().getCode(), resultProcessor);
        }

        Optional<Invoice> invoice = purchasingClient.purchaseTrucks(order);

        return ServicesUtil.processOptional(invoice, COULD_NOT_PURCHASE, invoiceResultPool, resultProcessor);
    }

    private <T> T processPurchaseError(String formatErrStr, Object formatArg, ServiceResultProcessor<Invoice, T> resultProcessor) {

        String errMsg = String.format(formatErrStr, formatArg);
        MutableServiceResult<Invoice> result = ServicesUtil.createResult(false, errMsg, null, invoiceResultPool);

        return ServicesUtil.processResult(result, resultProcessor, invoiceResultPool);
    }

    public <T> T deploy(TruckGarage truckGarage, ResultProcessor<T> resultProcessor) {

        if(!checkPreexisting(truckGarage.getTruck())) {
            return processResult(NONEXISTENT_TRUCK, truckGarage.getTruck().getVin(), resultProcessor);
        }

        if(!validate(truckGarage.getGarage())) {
            return processResult(INVALID_GARAGE, truckGarage.getGarage().getCode(), resultProcessor);
        }

        if(alreadyDeployed(truckGarage)) {
            String errMsg = String.format(ALREADY_DEPLOYED, truckGarage.getTruck().getVin(), truckGarage.getGarage().getCode());
            MutableResult result = ServicesUtil.createResult(false, errMsg, resultPool);

            return ServicesUtil.processResult(result, resultProcessor, resultPool);
        }

        Result result = deploymentClient.deployTruck(truckGarage);

        return resultProcessor.apply(result);
    }

    public <T> T patrol(boolean alcoholic, Neighborhood neighborhood, ResultProcessor<T> resultProcessor) {

        boolean success = deploymentClient.patrol(alcoholic, neighborhood);

        Result result;

        if(success) {
            result = ResultFactory.createResult(true, "success");
        }
        else {
            result = ResultFactory.createResult(false, COULD_NOT_PATROL);
        }

        return resultProcessor.apply(result);
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

    private boolean validate(Garage garage) {

        String garageCode = garage.getCode();

        if(garageCache.containsKey(garageCode)) {
            return true;
        }

        Optional<Garage> optional = garageCRUD.findByCode(garageCode);

        if(!optional.isPresent()) {
            return false;
        }

        garageCache.put(garageCode, optional.get());

        return true;
    }

    private Truck findPreexisting(Set<Truck> trucks) {

        for(Truck truck : trucks) {

            if(checkPreexisting(truck)) {
                return truck;
            }
        }

        return null;
    }

    private boolean checkPreexisting(Truck truck) {

        Optional optional = truckCRUD.findByVIN(truck.getVin());
        return optional.isPresent();
    }

    private Set<Truck> toTruckSet(Set<TruckGarage> garageTrucks) {

        Set<Truck> truckSet = new HashSet<>();

        garageTrucks.forEach(gt -> truckSet.add(gt.getTruck()));

        return truckSet;
    }

    private boolean alreadyDeployed(TruckGarage truckGarage) {

        Optional<TruckGarage> current = garageCRUD.findTruckGarageByTruck(truckGarage.getTruck());

        if(!current.isPresent()) {
            return false;
        }

        return current.get().equals(truckGarage);
    }

    private <T> T processResult(String formatErrStr, Object formatArg, ResultProcessor<T> resultProcessor) {

        String errMsg = String.format(formatErrStr, formatArg);
        MutableResult result = ServicesUtil.createResult(false, errMsg, resultPool);

        return ServicesUtil.processResult(result, resultProcessor, resultPool);
    }
}
