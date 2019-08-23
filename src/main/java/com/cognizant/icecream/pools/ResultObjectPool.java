package com.cognizant.icecream.pools;

import com.cognizant.icecream.pools.api.ResultPool;
import com.cognizant.icecream.pools.config.DefaultAbandonedConfig;
import com.cognizant.icecream.result.MutableResult;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class ResultObjectPool extends Pool<MutableResult> implements ResultPool {

    @Autowired
    ResultObjectPool(
            PooledObjectFactory<MutableResult> factory,
            GenericObjectPoolConfig<MutableResult> config,
            DefaultAbandonedConfig<MutableResult> abandonedConfig
    ) {
        super(factory, config, abandonedConfig, ResultObjectPool.class);
    }

    public MutableResult createResult(boolean success, String message)
            throws PoolCapacityException, Exception
    {
        MutableResult result = getObject();

        result.setSuccess(success);
        result.setMessage(message);

        return result;
    }
}
