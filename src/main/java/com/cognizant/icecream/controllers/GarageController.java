package com.cognizant.icecream.controllers;

import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import com.cognizant.icecream.services.GarageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static com.cognizant.icecream.controllers.ControllerUtil.checkPathVariableMatch;

@RestController
@RequestMapping("icecream/garage")
public class GarageController {

    private GarageService service;

    @Autowired
    public GarageController(GarageService service) {
        this.service = service;
    }

    @GetMapping("{code}")
    public ResponseEntity<Garage> getGarage(@PathVariable("code") String code) {

        Garage garage = service.getGarage(code);
        return new ResponseEntity<>(garage, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Garage> addGarage(@Valid @RequestBody Garage garage) {

        garage = service.addGarage(garage);
        return new ResponseEntity<>(garage, HttpStatus.CREATED);
    }

    @PutMapping("{code}")
    public ResponseEntity<?> updateGarage(@PathVariable("code") String code, @Valid @RequestBody Garage garage) {

        Optional<ResponseEntity<String>> errResponse = checkPathVariableMatch(code, garage::getCode, garage::setCode);

        if(errResponse.isPresent()) {
            return errResponse.get();
        }

        garage = service.updateGarage(garage);
        return new ResponseEntity<>(garage, HttpStatus.OK);
    }

    @DeleteMapping("{code}")
    public ResponseEntity<?> removeGarage(@PathVariable("code") String code) {

        Result result = service.removeGarage(code);

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @PostMapping("{code}/resupply")
    public ResponseEntity<Result> scheduleResupply(@PathVariable("code") String code, @Valid @RequestBody TimeSlot timeSlot) {

        Result result = service.resupply(code, timeSlot);
        return ControllerUtil.resultToResponseDefault(result);
    }
}
