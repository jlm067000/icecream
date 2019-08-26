package com.cognizant.icecream.controllers;

import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.result.ServiceResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ControllerUtil {

    static ResponseEntity<Result> processResult(Result result) {

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    static ResponseEntity<Result> processResult(Result result, HttpStatus successCode, HttpStatus failCode) {

        if(result.isSuccess()) {
            return new ResponseEntity<>(successCode);
        }
        else {
            return new ResponseEntity<>(result, failCode);
        }
    }

    static ResponseEntity<?> processResult(ServiceResult<?> result) {

        return processResult(result, HttpStatus.OK, HttpStatus.BAD_REQUEST);
    }

    static ResponseEntity<?> processResult(ServiceResult<?> result, HttpStatus successCode, HttpStatus failCode) {

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), successCode);
        }
        else {
            return new ResponseEntity<>(result.getMessage(), failCode);
        }
    }

    static ResponseEntity<?> processRetrievalResult(ServiceResult<?> result) {

        return processResult(result, HttpStatus.OK, HttpStatus.NOT_FOUND);
    }

    static ResponseEntity<?> processCreationResult(ServiceResult<?> result) {

        return processResult(result, HttpStatus.CREATED, HttpStatus.OK);
    }
}
