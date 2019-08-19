package com.cognizant.icecream.models.result;

public interface ServiceResult<T> extends Result {
    T getPayload();
}
