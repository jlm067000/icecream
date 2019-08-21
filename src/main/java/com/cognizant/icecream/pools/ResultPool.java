package com.cognizant.icecream.pools;

import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.MutableServiceResult;
import com.cognizant.icecream.result.ServiceResult;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
class ResultPool extends Pool<ServiceResult, MutableServiceResult> implements ServiceResultPool<ServiceResult> {

    @Autowired
    ResultPool(
            PooledObjectFactory<MutableServiceResult> factory,
            PooledObjectFactory<PoolKey<ServiceResult>> keyFactory,
            GenericObjectPoolConfig<MutableServiceResult> config,
            GenericObjectPoolConfig<PoolKey<ServiceResult>> keyConfig
    ) {
        super(factory, keyFactory, config, keyConfig);
    }

     public <T> PoolKey<ServiceResult> createResult(boolean success, String message, T payload) throws Exception {

        PoolKey<ServiceResult> key = reservePooledObject();

        if(key == null) {
            return addUnpooledObject(success, message, payload);
        }

        Consumer<MutableServiceResult> updater = p -> populate(p, success, message, payload);

        updateObject(key, updater);

        return key;
     }

     private <T> PoolKey<ServiceResult> addUnpooledObject(boolean success, String message, T payload) throws Exception {

        MutableServiceResult<T> result = new ServiceResultObject<>();
        result.setPayload(payload);
        result.setMessage(message);
        result.setIsSuccess(success);

        return addUnpooledObject(result);
     }

     private <T> void populate(MutableServiceResult<T> result, boolean success, String message, T payload) {

        result.setIsSuccess(success);
        result.setMessage(message);
        result.setPayload(payload);
     }
}
