package com.cognizant.icecream.controllers;

import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.result.ServiceResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

class ControllerUtil {

    static ResponseEntity<Result> processResult(Result result) {

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    static ResponseEntity<?> processResult(ServiceResult<?> result) {

        return processResult(result, HttpStatus.BAD_REQUEST);
    }

    static ResponseEntity<?> processRetrievalResult(ServiceResult<?> result) {

        return processResult(result, HttpStatus.NOT_FOUND);
    }

    static ResponseEntity<?> processResult(ServiceResult<?> result, HttpStatus failCode) {

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(result.getMessage(), failCode);
        }
    }
}
