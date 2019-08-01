package com.cognizant.icecream.controllers;

import com.cognizant.icecream.models.Invoice;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TruckGarage;
import com.cognizant.icecream.models.TruckPurchaseOrder;
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
    public ResponseEntity<String> getResponse() {

        return new ResponseEntity("Unavailable", HttpStatus.ACCEPTED);
    }

    @PostMapping("purchase")
    public ResponseEntity<Invoice> purchaseTrucks(@RequestParam("authorization") String auth, @RequestBody TruckPurchaseOrder order) {

        Invoice invoice = service.purchaseTrucks(auth, order);
        return new ResponseEntity<>(invoice, HttpStatus.ACCEPTED);
    }

    @GetMapping("location")
    public ResponseEntity<Set<Garage>> getLocations() {

        return new ResponseEntity(service.getLocations(), HttpStatus.ACCEPTED);
    }

    @PostMapping("deploy")
    public ResponseEntity<Result> deploy(@RequestBody TruckGarage truckLocation) {

        Result result = service.deploy(truckLocation);

        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }


}
