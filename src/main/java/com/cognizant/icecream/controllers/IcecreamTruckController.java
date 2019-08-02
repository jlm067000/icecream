package com.cognizant.icecream.controllers;

import com.cognizant.icecream.models.*;
import com.cognizant.icecream.services.truck.IcecreamTruckService;
import com.cognizant.icecream.services.truck.contract.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("icecream/truck")
public class IcecreamTruckController {

    @Autowired
    private IcecreamTruckService service;

    @GetMapping
    public ResponseEntity<Set<Truck>> getTrucks() {

        Set<Truck> trucks = service.getTrucks();
        return new ResponseEntity<>(trucks, HttpStatus.ACCEPTED);
    }

    @GetMapping("garage/{code}")
    public ResponseEntity<Set<Truck>> getTrucks(@PathVariable("code") String garageCode) {

        Set<Truck> trucks = service.getTrucks(garageCode);
        return new ResponseEntity<>(trucks, HttpStatus.ACCEPTED);
    }

    @GetMapping("garage/{code}/alcoholic")
    public ResponseEntity<Set<Truck>> getAlcoholicTrucks(@RequestParam("code") String garageCode) {

        Set<Truck> trucks = service.getTrucks(garageCode, true);
        return new ResponseEntity<>(trucks, HttpStatus.ACCEPTED);
    }

    @GetMapping("garage/{code}/nonalcoholic")
    public ResponseEntity<Set<Truck>> getNonAlcoholicTrucks(@PathVariable("code") String garageCode) {

        Set<Truck> trucks = service.getTrucks(garageCode, false);
        return new ResponseEntity<>(trucks, HttpStatus.ACCEPTED);
    }

    @GetMapping("alcoholic")
    public ResponseEntity<Set<Truck>> getAlcoholicTrucks() {

        Set<Truck> trucks = service.getTrucks(true);
        return new ResponseEntity<>(trucks, HttpStatus.ACCEPTED);
    }

    @GetMapping("nonalcoholic")
    public ResponseEntity<Set<Truck>> getNonAlcoholicTrucks() {

        Set<Truck> trucks = service.getTrucks(false);
        return new ResponseEntity<>(trucks, HttpStatus.ACCEPTED);
    }

    @PostMapping("purchase")
    public ResponseEntity<Invoice> purchaseTrucks(@RequestParam("authorization") String auth, @RequestBody TruckPurchaseOrder order) {

        Invoice invoice = service.purchaseTrucks(auth, order);
        return new ResponseEntity<>(invoice, HttpStatus.ACCEPTED);
    }

    @PostMapping("deploy")
    public ResponseEntity<Result> deploy(@RequestBody TruckGarage truckGarage) {

        Result result = service.deploy(truckGarage);
        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }

    @PostMapping("patrol/alcoholic")
    public ResponseEntity<Result> patrolAlcoholic(@RequestBody Neighborhood neighborhood) {

        Result result = service.patrol(true, neighborhood);
        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }

    @PostMapping("patrol/nonalcoholic")
    public ResponseEntity<Result> patrolNonAlcoholic(@RequestBody Neighborhood neighborhood) {

        Result result = service.patrol(false, neighborhood);
        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }

    @PostMapping("garage/{code}/resupply")
    public ResponseEntity<Result> scheduleResupply(@PathVariable String garageCode, @RequestBody TimeSlot timeSlot) {

        Result result = service.resupply(garageCode, timeSlot);
        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }

}
