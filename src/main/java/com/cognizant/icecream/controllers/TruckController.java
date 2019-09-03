package com.cognizant.icecream.controllers;

import com.cognizant.icecream.api.models.*;
import com.cognizant.icecream.api.result.Result;
import com.cognizant.icecream.api.result.ServiceResultProcessor;
import com.cognizant.icecream.services.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.cognizant.icecream.controllers.ResultProcessorFactory.createServiceResultProcessor;

@RestController
@RequestMapping("icecream/truck")
public class TruckController {

    private static final String PATH_VARIABLE_MISMATCH = "The Request Body does not match path variable: vin";

    private TruckService service;
    private ServiceResultProcessor<Truck, ResponseEntity<?>> retrievalProcessor;
    private ServiceResultProcessor<Truck, ResponseEntity<?>> addProcessor;
    private ServiceResultProcessor<Truck, ResponseEntity<?>> updateProcessor;
    private ServiceResultProcessor<Truck, ResponseEntity<?>> defaultProcessor;
    private ServiceResultProcessor<Invoice, ResponseEntity<?>> invoiceProcessor;
    private ServiceResultProcessor<Set<Truck>, ResponseEntity<?>> setRetrievalProcessor;

    @Autowired
    public TruckController(TruckService service) {

        this.service = service;
        initialize();
    }

    private void initialize() {
        retrievalProcessor = createServiceResultProcessor(HttpStatus.OK, HttpStatus.NOT_FOUND);
        addProcessor = createServiceResultProcessor(HttpStatus.CREATED, HttpStatus.OK);
        updateProcessor = createServiceResultProcessor(HttpStatus.OK, HttpStatus.NOT_FOUND);
        defaultProcessor = createServiceResultProcessor(HttpStatus.OK, HttpStatus.BAD_REQUEST);
        invoiceProcessor = createServiceResultProcessor(HttpStatus.OK, HttpStatus.BAD_REQUEST);
        setRetrievalProcessor = createServiceResultProcessor(HttpStatus.OK, HttpStatus.NOT_FOUND);
    }

    @GetMapping("{vin}")
    public ResponseEntity<?> getTruck(@PathVariable("vin") String vin) {

        return service.getTruck(vin, retrievalProcessor);
    }

    @PostMapping
    public ResponseEntity<?> addTruck(@Valid @RequestBody Truck truck) {

        return service.addTruck(truck, addProcessor);
    }

    @PutMapping("{vin}")
    public ResponseEntity<?> updateTruck(@PathVariable("vin") String vin, @Valid @RequestBody Truck truck) {

        ResponseEntity<String> mismatchResponse = checkPathVariableMismatch(vin, truck, PATH_VARIABLE_MISMATCH);

        if(mismatchResponse != null) {
            return mismatchResponse;
        }

        return service.updateTruck(truck, updateProcessor);
    }

    @DeleteMapping("{vin}")
    public ResponseEntity<?> removeTruck(@PathVariable("vin") String vin) {

        return service.removeTruck(vin, ResultProcessorFactory.REMOVE_PROCESSOR);
    }

    @GetMapping
    public ResponseEntity<Set<Truck>> getTrucks() {

        Set<Truck> trucks = service.getTrucks();

        if(trucks == null) {
            trucks = new HashSet<>();
        }

        ResponseEntity<Set<Truck>> response = new ResponseEntity<>(trucks, HttpStatus.OK);

        return response;
    }

    @GetMapping("garage/{code}")
    public ResponseEntity<Set<Truck>> getTrucks(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("code") String garageCode
    ) {
        Set<Truck> trucks = service.getTrucks(authorization, garageCode);
        return new ResponseEntity<>(trucks, HttpStatus.OK);
    }

    @GetMapping("garage/{code}/alcoholic")
    public ResponseEntity<Set<Truck>> getAlcoholicTrucks(
                                            @RequestHeader("Authorization") String authorization,
                                            @PathVariable("code") String garageCode
    ) {
        Set<Truck> trucks = service.getTrucks(authorization, garageCode, true);
        return new ResponseEntity<>(trucks, HttpStatus.OK);
    }

    @GetMapping("garage/{code}/nonalcoholic")
    public ResponseEntity<Set<Truck>> getNonAlcoholicTrucks(
                                        @RequestHeader("Authorization") String authorization,
                                        @PathVariable("code") String garageCode
    ) {
        Set<Truck> trucks = service.getTrucks(authorization, garageCode, false);
        return new ResponseEntity<>(trucks, HttpStatus.OK);
    }

    @GetMapping("alcoholic")
    public ResponseEntity<Set<Truck>> getAlcoholicTrucks() {

        Set<Truck> trucks = service.getTrucks(true);
        return new ResponseEntity<>(trucks, HttpStatus.OK);
    }

    @GetMapping("nonalcoholic")
    public ResponseEntity<Set<Truck>> getNonAlcoholicTrucks() {

        Set<Truck> trucks = service.getTrucks(false);
        return new ResponseEntity<>(trucks, HttpStatus.OK);
    }

    @PostMapping("purchase")
    public ResponseEntity<?> purchaseTrucks(@RequestHeader("Authorization") String auth, @Valid @RequestBody TruckPurchaseOrder order) {

        return service.purchaseTrucks(auth, order, invoiceProcessor);
    }

    @PostMapping("deploy")
    public ResponseEntity<Result> deploy(
                                    @RequestHeader("Authorization") String authorization,
                                    @Valid @RequestBody TruckGarage truckGarage
    ) {

        return service.deploy(authorization, truckGarage, ResultProcessorFactory.DEFAULT_PROCESSOR);
    }

    @PostMapping("patrol")
    public ResponseEntity<Result> patrol(@RequestParam("alcoholic") boolean alcoholic, @RequestBody Neighborhood neighborhood) {

        return service.patrol(alcoholic, neighborhood, ResultProcessorFactory.DEFAULT_PROCESSOR);
    }

    private static ResponseEntity<String> checkPathVariableMismatch(String pathVariable, Truck truck, String errorMsg)
    {
        if(truck.getVin() == null) {
            truck.setVin(pathVariable);
            return null;
        }

        if(Objects.equals(truck.getVin(), pathVariable)) {
            return null;
        }

        return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
    }
}
