package com.cognizant.icecream.pools.api;

import com.cognizant.icecream.pools.PoolCapacityException;
import com.cognizant.icecream.result.MutableServiceResult;

public interface ServiceResultPool<T> extends LocalObjectPool<MutableServiceResult<T>> {

    MutableServiceResult<T> createResult(boolean success, String message, T payload) throws PoolCapacityException, Exception;
}
