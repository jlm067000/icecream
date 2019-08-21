package com.cognizant.icecream.controllers;

import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.result.ResultFactory;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.cognizant.icecream.controllers.ControllerUtil.processResult;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ControllerUtilTest {

    @Test
    public void testResultToResponse() {

        Result result = ResultFactory.createResult(true, "");

        ResponseEntity<Result> response = processResult(result);

        assertNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        result = ResultFactory.createResult(false, "");
        response = processResult(result);

        assertEquals(result, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
