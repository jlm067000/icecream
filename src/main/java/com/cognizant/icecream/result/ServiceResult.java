package com.cognizant.icecream.result;

public interface ServiceResult<T> extends Result {

    T getPayload();
}
