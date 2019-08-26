package com.cognizant.icecream.controllers;

import com.cognizant.icecream.models.*;
import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.services.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("icecream/truck")
public class TruckController {

    private static final String PATH_VARIABLE_MISMATCH = "The Request Body does not match path variable: vin";

    private TruckService service;

    @Autowired
    public TruckController(TruckService service) {
        this.service = service;
    }

    @GetMapping("{vin}")
    public ResponseEntity<?> getTruck(@PathVariable("vin") String vin) {

        return service.getTruck(vin, ResultProcessorFactory.GET_PROCESSOR);
    }

    @PostMapping
    public ResponseEntity<?> addTruck(@Valid @RequestBody Truck truck) {

        return service.addTruck(truck, ResultProcessorFactory.GET_PROCESSOR);
    }

    @PutMapping("{vin}")
    public ResponseEntity<?> updateTruck(@PathVariable("vin") String vin, @Valid @RequestBody Truck truck) {

        ResponseEntity<String> mismatchResponse = checkPathVariableMismatch(vin, truck, PATH_VARIABLE_MISMATCH);

        if(mismatchResponse != null) {
            return mismatchResponse;
        }

        return service.updateTruck(truck, ResultProcessorFactory.SERVICE_RESULT_PROCESSOR);
    }

    @DeleteMapping("{vin}")
    public ResponseEntity<?> removeTruck(@PathVariable("vin") String vin) {

        return service.removeTruck(vin, ResultProcessorFactory.REMOVE_PROCESSOR);
    }

    @GetMapping
    public ResponseEntity<Set<Truck>> getTrucks() {

        Set<Truck> trucks = service.getTrucks();
        return new ResponseEntity<>(trucks, HttpStatus.OK);
    }

    @GetMapping("garage/{code}")
    public ResponseEntity<Set<Truck>> getTrucks(@PathVariable("code") String garageCode) {

        Set<Truck> trucks = service.getTrucks(garageCode);
        return new ResponseEntity<>(trucks, HttpStatus.OK);
    }

    @GetMapping("garage/{code}/alcoholic")
    public ResponseEntity<Set<Truck>> getAlcoholicTrucks(@PathVariable("code") String garageCode) {

        Set<Truck> trucks = service.getTrucks(garageCode, true);
        return new ResponseEntity<>(trucks, HttpStatus.OK);
    }

    @GetMapping("garage/{code}/nonalcoholic")
    public ResponseEntity<Set<Truck>> getNonAlcoholicTrucks(@PathVariable("code") String garageCode) {

        Set<Truck> trucks = service.getTrucks(garageCode, false);
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
    public ResponseEntity<Invoice> purchaseTrucks(@RequestHeader("Authorization") String auth, @Valid @RequestBody TruckPurchaseOrder order) {

        Invoice invoice = service.purchaseTrucks(auth, order);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @PostMapping("deploy")
    public ResponseEntity<?> deploy(@Valid @RequestBody TruckGarage truckGarage) {

        Result result = service.deploy(truckGarage);
        return ResultProcessorFactory.DEFAULT_PROCESSOR.apply(result);
    }

    @PostMapping("patrol")
    public ResponseEntity<Result> patrolAlcoholic(@RequestParam boolean alcoholic, @RequestBody Neighborhood neighborhood) {

        Result result = service.patrol(alcoholic, neighborhood);
        return ResultProcessorFactory.DEFAULT_PROCESSOR.apply(result);
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
