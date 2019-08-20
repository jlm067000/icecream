package com.cognizant.icecream.pools;

import com.cognizant.icecream.models.result.MutableServiceResult;

public class ServiceResultObject<T> implements MutableServiceResult<T> {

    private boolean isSuccess;
    private String message;
    private T payload;


    @Override
    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setPayload(T payload) {
        this.payload = payload;
    }

    @Override
    public T getPayload() {
        return payload;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }
}
