package com.cognizant.icecream.pools;

import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.pools.config.abandoned.DefaultAbandonedConfig;
import com.cognizant.icecream.result.MutableServiceResult;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

abstract class ServiceResultObjectPool<T> extends Pool<MutableServiceResult<T>> implements ServiceResultPool<T> {

    ServiceResultObjectPool(
            PooledObjectFactory<MutableServiceResult<T>> factory,
            GenericObjectPoolConfig<MutableServiceResult<T>> config,
            DefaultAbandonedConfig<MutableServiceResult<T>> abandonedConfig,
            Class<? extends Pool<MutableServiceResult<T>>> poolType
    ) {
        super(factory, config, abandonedConfig, poolType);
    }

    @Override
    public MutableServiceResult<T> createResult(boolean success, String message, T payload)
            throws PoolCapacityException, Exception
    {

        MutableServiceResult<T> result = getObject();

        result.setIsSuccess(success);
        result.setMessage(message);
        result.setPayload(payload);

        return result;
    }
}
