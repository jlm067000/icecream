package com.cognizant.icecream.controllers;

import com.cognizant.icecream.models.result.ClientResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

class ControllerUtil {

    static <T> Optional<ResponseEntity<String>> checkPathVariableMatch(
            T pathVariable,
            Supplier<T> getter,
            Consumer<T> setter)
    {

        if(getter.get() == null) {
            setter.accept(pathVariable);
            return Optional.empty();
        }

        if(Objects.equals(getter.get(), pathVariable)) {
            return Optional.empty();
        }

        String errorMsg = "Path variable " + pathVariable + " does not match Request Body.";
        ResponseEntity<String> response = new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);

        return Optional.of(response);
    }

    static ResponseEntity<ClientResult> resultToResponseDefault(ClientResult result) {

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

}
