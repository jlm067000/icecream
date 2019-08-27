package com.cognizant.icecream.pools;

import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.pools.api.LocalObjectPool;
import com.cognizant.icecream.pools.config.DefaultAbandonedConfig;
import com.cognizant.icecream.result.MutableServiceResult;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TruckSetResultPool extends ServiceResultObjectPool<Set<Truck>> implements LocalObjectPool<MutableServiceResult<Set<Truck>>> {

    @Autowired
    TruckSetResultPool(
            PooledObjectFactory<MutableServiceResult<Set<Truck>>> factory,
            GenericObjectPoolConfig<MutableServiceResult<Set<Truck>>> config,
            DefaultAbandonedConfig<MutableServiceResult<Set<Truck>>> abandonedConfig
    ) {
        super(factory, config, abandonedConfig, TruckSetResultPool.class);
    }
}
