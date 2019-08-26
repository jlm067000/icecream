package com.cognizant.icecream.pools;

import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.pools.api.LocalObjectPool;
import com.cognizant.icecream.pools.config.DefaultAbandonedConfig;
import com.cognizant.icecream.result.MutableServiceResult;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TruckResultObjectPool extends ServiceResultObjectPool<Truck> implements LocalObjectPool<MutableServiceResult<Truck>>  {

    @Autowired
    TruckResultObjectPool(
            PooledObjectFactory<MutableServiceResult<Truck>> factory,
            GenericObjectPoolConfig<MutableServiceResult<Truck>> config,
            DefaultAbandonedConfig<MutableServiceResult<Truck>> abandonedConfig
    ) {
        super(factory, config, abandonedConfig, TruckResultObjectPool.class);
    }
}
