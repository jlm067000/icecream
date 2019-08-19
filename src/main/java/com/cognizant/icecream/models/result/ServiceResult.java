package com.cognizant.icecream.models.result;

public interface ServiceResult<T> extends ClientResult {
    T getPayload();
}
