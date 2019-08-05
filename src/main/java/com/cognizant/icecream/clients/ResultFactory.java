package com.cognizant.icecream.clients;

public class ResultFactory {

    public static Result createResult(boolean success, String message) {

        return new ResultObject(success, message);
    }
}
