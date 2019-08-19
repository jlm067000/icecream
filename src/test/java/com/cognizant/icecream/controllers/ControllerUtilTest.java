package com.cognizant.icecream.controllers;

import com.cognizant.icecream.models.result.ClientResult;
import com.cognizant.icecream.clients.result.ClientResultFactory;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.cognizant.icecream.controllers.ControllerUtil.checkPathVariableMatch;
import static com.cognizant.icecream.controllers.ControllerUtil.resultToResponseDefault;
import static org.junit.Assert.*;

public class ControllerUtilTest {


    @Test
    public void testPathVariableMatch() {

        Object pathVariable = new Object();
        Body body = new Body();

        Optional<ResponseEntity<String>> response = checkPathVariableMatch(pathVariable, body, body);

        assertEquals(pathVariable, body.get());
        assertTrue(response.isEmpty());

        body.accept(pathVariable);
        response = checkPathVariableMatch(pathVariable, body, body);

        assertEquals(pathVariable, body.get());
        assertTrue(response.isEmpty());

        body.accept(new Object());
        response = checkPathVariableMatch(pathVariable, body, body);

        assertNotEquals(pathVariable, body.get());
        assertTrue(response.isPresent());
        assertEquals(HttpStatus.BAD_REQUEST, response.get().getStatusCode());
    }


    private static class Body implements Consumer<Object>, Supplier<Object> {

        private Object variable;

        @Override
        public void accept(Object variable) {
            this.variable = variable;
        }

        @Override
        public Object get() {
            return variable;
        }
    }

    @Test
    public void testResultToResponse() {

        ClientResult result = ClientResultFactory.createResult(true, "");

        ResponseEntity<ClientResult> response = resultToResponseDefault(result);

        assertNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        result = ClientResultFactory.createResult(false, "");
        response = resultToResponseDefault(result);

        assertEquals(result, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
