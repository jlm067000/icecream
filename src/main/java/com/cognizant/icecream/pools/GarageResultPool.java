package com.cognizant.icecream.pools;

import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.pools.api.LocalObjectPool;
import com.cognizant.icecream.pools.config.DefaultAbandonedConfig;
import com.cognizant.icecream.result.MutableServiceResult;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class GarageResultPool extends ServiceResultObjectPool<Garage> implements LocalObjectPool<MutableServiceResult<Garage>> {

    @Autowired
    GarageResultPool(
            PooledObjectFactory<MutableServiceResult<Garage>> factory,
            GenericObjectPoolConfig<MutableServiceResult<Garage>> config,
            DefaultAbandonedConfig<MutableServiceResult<Garage>> abandonedConfig
    ) {
        super(factory, config, abandonedConfig, GarageResultPool.class);
    }
}
