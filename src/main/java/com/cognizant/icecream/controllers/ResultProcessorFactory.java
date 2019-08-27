package com.cognizant.icecream.controllers;

import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.result.ServiceResult;
import com.cognizant.icecream.result.ResultProcessor;
import com.cognizant.icecream.result.ServiceResultProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResultProcessorFactory {

    static final ResultProcessor<ResponseEntity<Result>> DEFAULT_PROCESSOR;
    static final ResultProcessor<ResponseEntity<Result>> REMOVE_PROCESSOR;
    static final ServiceResultProcessor<?, ResponseEntity<?>> GET_PROCESSOR;
    static final ServiceResultProcessor<?, ResponseEntity<?>> ADD_PROCESSOR;
    static final ServiceResultProcessor<?,ResponseEntity<?>> SERVICE_RESULT_PROCESSOR;

    static {
        DEFAULT_PROCESSOR = ResultProcessorFactory::processResult;
        REMOVE_PROCESSOR = r -> processResult(r, HttpStatus.OK, HttpStatus.NO_CONTENT);
        GET_PROCESSOR = r -> processServiceResult(r, HttpStatus.OK, HttpStatus.NOT_FOUND);
        ADD_PROCESSOR = r -> processServiceResult(r, HttpStatus.CREATED, HttpStatus.OK);
        SERVICE_RESULT_PROCESSOR = r -> processServiceResult(r, HttpStatus.OK, HttpStatus.BAD_REQUEST);
    }

    static ResultProcessor<ResponseEntity<?>> createResultProcessor(HttpStatus successCode, HttpStatus failCode) {

        ResultProcessor<ResponseEntity<?>> processor = r -> processResult(r, successCode, failCode);
        return processor;
    }

    static <T> ServiceResultProcessor<T, ResponseEntity<?>> createServiceResultProcessor(HttpStatus successCode,  HttpStatus failCode)
    {
        ServiceResultProcessor<T, ResponseEntity<?>> processor = r -> processServiceResult(r, successCode, failCode);
        return processor;
    }

    private static ResponseEntity<Result> processResult(Result result) {

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    private static ResponseEntity<Result> processResult(Result result, HttpStatus successCode, HttpStatus failCode) {

        if(result.isSuccess()) {
            return new ResponseEntity<>(successCode);
        }
        else {
            return new ResponseEntity<>(result, failCode);
        }
    }

    private static ResponseEntity<?> processServiceResult(ServiceResult result, HttpStatus successCode, HttpStatus failCode) {

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), successCode);
        }
        else {
            return new ResponseEntity<>(result.getMessage(), failCode);
        }
    }
}
