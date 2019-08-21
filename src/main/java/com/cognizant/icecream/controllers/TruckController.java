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

import static com.cognizant.icecream.controllers.ControllerUtil.processResult;

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
    public ResponseEntity<Truck> getTruck(@PathVariable("vin") String vin) {

        Truck truck = service.getTruck(vin);
        return new ResponseEntity<>(truck, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Truck> addTruck(@Valid @RequestBody Truck truck) {

        truck = service.addTruck(truck);
        return new ResponseEntity<>(truck, HttpStatus.CREATED);
    }

    @PutMapping("{vin}")
    public ResponseEntity<?> updateTruck(@PathVariable("vin") String vin, @Valid @RequestBody Truck truck) {

        ResponseEntity<String> mismatchResponse = checkPathVariableMismatch(vin, truck, PATH_VARIABLE_MISMATCH);

        if(mismatchResponse != null) {
            return mismatchResponse;
        }

        truck = service.updateTruck(truck);
        return new ResponseEntity<>(truck, HttpStatus.OK);
    }

    @DeleteMapping("{vin}")
    public ResponseEntity<?> removeTruck(@PathVariable("vin") String vin) {

        Result result = service.removeTruck(vin);

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public ResponseEntity<Result> deploy(@Valid @RequestBody TruckGarage truckGarage) {

        Result result = service.deploy(truckGarage);
        return processResult(result);
    }

    @PostMapping("patrol")
    public ResponseEntity<Result> patrolAlcoholic(@RequestParam boolean alcoholic, @RequestBody Neighborhood neighborhood) {

        Result result = service.patrol(alcoholic, neighborhood);
        return processResult(result);
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
