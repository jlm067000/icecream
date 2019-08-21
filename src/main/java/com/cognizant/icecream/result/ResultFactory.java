package com.cognizant.icecream.result;

public class ResultFactory {

    public static Result createResult(boolean success, String message) {

        return new ResultObject(success, message);
    }
}
