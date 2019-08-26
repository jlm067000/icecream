package com.cognizant.icecream.controllers;

import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.result.ServiceResult;
import com.cognizant.icecream.result.ResultProcessor;
import com.cognizant.icecream.result.ServiceResultProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResultProcessorFactory {

    static final ResultProcessor<Result, ResponseEntity<?>> DEFAULT_RESULT_PROCESSOR;

    static {
        DEFAULT_RESULT_PROCESSOR = ResultProcessorFactory::processResult;
    }

    static ResultProcessor<Result, ResponseEntity<?>> defaultResultProcessor() {

        return ResultProcessorFactory::processResult;
    }

    static ResultProcessor<Result, ResponseEntity<?>> createResultProcessor(HttpStatus successCode, HttpStatus failCode) {

        ResultProcessor<Result, ResponseEntity<?>> processor = r -> processResult(r, successCode, failCode);
        return processor;
    }

    static <T> ServiceResultProcessor<T, ResponseEntity<?>> createServiceResultProcessor() {

        return ResultProcessorFactory::processServiceResult;
    }

    static <T> ServiceResultProcessor<T, ResponseEntity<?>> createServiceResultProcessor(HttpStatus successCode,  HttpStatus failCode)
    {
        ServiceResultProcessor<T, ResponseEntity<?>> processor = r -> processServiceResult(r, successCode, failCode);
        return ResultProcessorFactory::processResult;
    }

    static <T> ServiceResultProcessor<T, ResponseEntity<?>> defaultRetrievalResultProcessor() {

        return ResultProcessorFactory::processRetrievalResult;
    }

    private static <T> ResponseEntity<?> processRetrievalResult(ServiceResult<T> result) {

        return processResult(result, HttpStatus.OK, HttpStatus.NOT_FOUND);
    }

    static <T> ServiceResultProcessor<T, ResponseEntity<?>> getDefaultCreationResultProcessor() {

        return ResultProcessorFactory::processCreationResult;
    }

    private static <T> ResponseEntity<?> processCreationResult(ServiceResult<T> result) {

        return processResult(result, HttpStatus.CREATED, HttpStatus.OK);
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

    private static <T> ResponseEntity<?> processServiceResult(ServiceResult<T> result) {

        return processServiceResult(result, HttpStatus.OK, HttpStatus.BAD_REQUEST);
    }

    private static <T> ResponseEntity<?> processServiceResult(ServiceResult<T> result, HttpStatus successCode, HttpStatus failCode) {

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), successCode);
        }
        else {
            return new ResponseEntity<>(result.getMessage(), failCode);
        }
    }
}
