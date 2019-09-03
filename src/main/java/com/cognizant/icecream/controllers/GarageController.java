package com.cognizant.icecream.controllers;

import com.cognizant.icecream.api.models.Garage;
import com.cognizant.icecream.api.models.TimeSlot;
import com.cognizant.icecream.api.result.Result;
import com.cognizant.icecream.api.result.ServiceResultProcessor;
import com.cognizant.icecream.services.GarageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("icecream/garage")
public class GarageController {

    private static final String PATH_VARIABLE_MISMATCH = "The Request Body does not match path variable: code";

    private GarageService service;
    private ServiceResultProcessor<Garage, ResponseEntity<?>> retrievalProcessor;
    private ServiceResultProcessor<Garage, ResponseEntity<?>> addProcessor;
    private ServiceResultProcessor<Garage, ResponseEntity<?>> defaultProcessor;

    @Autowired
    public GarageController(GarageService service) {

        this.service = service;
        initializeResultProcessors();
    }

    private void initializeResultProcessors() {

        retrievalProcessor = ResultProcessorFactory.createServiceResultProcessor(HttpStatus.OK, HttpStatus.NOT_FOUND);
        addProcessor = ResultProcessorFactory.createServiceResultProcessor(HttpStatus.CREATED, HttpStatus.OK);
        defaultProcessor = ResultProcessorFactory.createServiceResultProcessor(HttpStatus.OK, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("{code}")
    public ResponseEntity<?> getGarage(@PathVariable("code") String code) {

        return service.getGarage(code, retrievalProcessor);
    }

    @PostMapping
    public ResponseEntity<?> addGarage(@Valid @RequestBody Garage garage) {

        return service.addGarage(garage, addProcessor);
    }

    @PutMapping("{code}")
    public ResponseEntity<?> updateGarage(@PathVariable("code") String code, @Valid @RequestBody Garage garage) {

        ResponseEntity<String> mismatchResponse = checkPathVariableMismatch(code, garage, PATH_VARIABLE_MISMATCH);

        if(mismatchResponse != null) {
            return mismatchResponse;
        }

        return service.updateGarage(garage, defaultProcessor);
    }

    @DeleteMapping("{code}")
    public ResponseEntity<Result> removeGarage(@PathVariable("code") String code) {

        return service.removeGarage(code, ResultProcessorFactory.REMOVE_PROCESSOR);
    }

    @PostMapping("{code}/resupply")
    public ResponseEntity<Result> scheduleResupply(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("code") String code,
            @Valid @RequestBody TimeSlot timeSlot
    ) {

        return service.resupply(authorization, code, timeSlot, ResultProcessorFactory.DEFAULT_PROCESSOR);
    }

    private static ResponseEntity<String> checkPathVariableMismatch(String pathVariable, Garage garage, String errorMsg)
    {
        if(garage.getCode() == null) {
            garage.setCode(pathVariable);
            return null;
        }

        if(Objects.equals(garage.getCode(), pathVariable)) {
            return null;
        }

        return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
    }
}
