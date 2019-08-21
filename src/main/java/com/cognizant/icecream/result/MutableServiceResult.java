package com.cognizant.icecream.result;

public interface MutableServiceResult<T> extends ServiceResult<T> {

    void setIsSuccess(boolean isSuccess);
    void setMessage(String message);
    void setPayload(T payload);
}
