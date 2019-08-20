package com.cognizant.icecream.pools;

import com.cognizant.icecream.models.result.MutableServiceResult;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.EvictionPolicy;

import java.util.function.Consumer;

abstract class ServiceResultPool<T> extends Pool<MutableServiceResult<T>> {


    ServiceResultPool(
            int size,
            PooledObjectFactory<MutableServiceResult<T>> factory,
            PooledObjectFactory<PoolKey<MutableServiceResult<T>>> keyFactory,
            EvictionPolicy<MutableServiceResult<T>> evictionPolicy,
            EvictionPolicy<PoolKey<MutableServiceResult<T>>> keyEvictionPolicy
    ) {
        super(size, factory, keyFactory, evictionPolicy, keyEvictionPolicy);
    }

     public PoolKey<MutableServiceResult<T>> createResult(boolean success, String message, T payload) throws Exception {

        PoolKey<MutableServiceResult<T>> key = reservePooledObject();

        if(key == null) {
            return addUnpooledObject(success, message, payload);
        }

        Consumer<MutableServiceResult<T>> updater = p -> populate(p, success, message, payload);

        updateObject(key, updater);

        return key;
     }

     private PoolKey<MutableServiceResult<T>> addUnpooledObject(boolean success, String message, T payload) throws Exception {

        MutableServiceResult<T> result = new ServiceResultObject<>();
        result.setPayload(payload);
        result.setMessage(message);
        result.setIsSuccess(success);

        return addUnpooledObject(result);
     }

     private void populate(MutableServiceResult<T> result, boolean success, String message, T payload) {

        result.setIsSuccess(success);
        result.setMessage(message);
        result.setPayload(payload);
     }

}
