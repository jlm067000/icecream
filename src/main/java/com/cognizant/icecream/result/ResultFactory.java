package com.cognizant.icecream.result;

public class ResultFactory {

    public static Result createResult(boolean success, String message) {

        return new ResultObject(success, message);
    }

    public static <T> ServiceResult<T> createServiceResult(boolean success, String message, T payload) {

        return createMutableServiceResult();
    }

    public static <T> MutableServiceResult<T> createMutableServiceResult(boolean success, String message, T payload) {

        MutableServiceResult<T> result = new ServiceResultObject<>();

        result.setIsSuccess(success);
        result.setMessage(message);
        result.setPayload(payload);

        return result;
    }

    public static <T> MutableServiceResult<T> createMutableServiceResult() {

        return new ServiceResultObject<>();
    }
}
