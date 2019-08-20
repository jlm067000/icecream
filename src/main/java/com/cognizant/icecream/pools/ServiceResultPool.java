package com.cognizant.icecream.pools;

import com.cognizant.icecream.models.result.MutableServiceResult;

import java.util.Map;

abstract class ServiceResultPool<T> {

    private int size;
    private Map<Integer, MutableServiceResult<T>> borrowedResults;

    ServiceResultPool(int size) {
        this.size = size;
    }

    public abstract Integer createResult(boolean success, String message, T payload);

}
