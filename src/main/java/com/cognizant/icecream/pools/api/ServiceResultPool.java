package com.cognizant.icecream.pools.api;

import com.cognizant.icecream.pools.PoolKey;
import com.cognizant.icecream.result.ServiceResult;

public interface ServiceResultPool<T> extends LocalObjectPool<T> {

    <V> PoolKey<ServiceResult> createResult(boolean success, String message, V payload) throws Exception;
}
