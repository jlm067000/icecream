package com.cognizant.icecream.controllers;

import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.result.ServiceResult;
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

    @Autowired
    public GarageController(GarageService service) {
        this.service = service;
    }

    @GetMapping("{code}")
    public ResponseEntity<?> getGarage(@PathVariable("code") String code) {

        return service.getGarage(code, ResultProcessorFactory.GET_PROCESSOR);
    }

    @PostMapping
    public ResponseEntity<?> addGarage(@Valid @RequestBody Garage garage) {

        return service.addGarage(garage, ResultProcessorFactory.ADD_PROCESSOR);
    }

    @PutMapping("{code}")
    public ResponseEntity<?> updateGarage(@PathVariable("code") String code, @Valid @RequestBody Garage garage) {

        ResponseEntity<String> mismatchResponse = checkPathVariableMismatch(code, garage, PATH_VARIABLE_MISMATCH);

        if(mismatchResponse != null) {
            return mismatchResponse;
        }

        return service.updateGarage(garage, ResultProcessorFactory.SERVICE_RESULT_PROCESSOR);
    }

    @DeleteMapping("{code}")
    public ResponseEntity<Result> removeGarage(@PathVariable("code") String code) {

        return service.removeGarage(code, ResultProcessorFactory.REMOVE_PROCESSOR);
    }

    @PostMapping("{code}/resupply")
    public ResponseEntity<Result> scheduleResupply(@PathVariable("code") String code, @Valid @RequestBody TimeSlot timeSlot) {

        Result result = service.resupply(code, timeSlot);
        return ResultProcessorFactory.DEFAULT_PROCESSOR.apply(result);
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
