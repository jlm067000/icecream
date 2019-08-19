package com.cognizant.icecream.services.result;

import com.cognizant.icecream.models.result.ServiceResult;

class ResultObject<T> implements ServiceResult<T> {

    @Override
    public T getPayload() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }
}
