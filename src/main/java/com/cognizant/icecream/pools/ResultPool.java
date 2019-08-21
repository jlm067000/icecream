package com.cognizant.icecream.pools;

import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.MutableServiceResult;
import com.cognizant.icecream.result.ServiceResult;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
class ResultPool extends Pool<ServiceResult, MutableServiceResult> implements ServiceResultPool {

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

     @Override
     public <V> V processObject(PoolKey<ServiceResult> key, Function<ServiceResult, V> resultProcessor) {

        V processed = super.processObject(key, resultProcessor);

        if(processed instanceof MutableServiceResult) {
            return (V)clone((MutableServiceResult<?>)processed);
        }
        else {
            return processed;
        }
    }

    private <T> ServiceResult clone(ServiceResult<T> result) {

        MutableServiceResult<T> clone = new ServiceResultObject<>();
        clone.setIsSuccess(result.isSuccess());
        clone.setMessage(result.getMessage());
        clone.setPayload(result.getPayload());

        return clone;
    }
}
