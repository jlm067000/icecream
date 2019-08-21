package com.cognizant.icecream.pools.config;

import com.cognizant.icecream.pools.PoolKey;
import org.apache.commons.pool2.impl.EvictionPolicy;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

abstract class KeyPoolConfig<T> extends GenericObjectPoolConfig<PoolKey<T>> {

    KeyPoolConfig(EvictionPolicy<PoolKey<T>> evictionPolicy) {
        setEvictionPolicy(evictionPolicy);
    }
}
