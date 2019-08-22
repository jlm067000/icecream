package com.cognizant.icecream.pools.config;

import org.apache.commons.pool2.impl.EvictionPolicy;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

abstract class DefaultObjectPoolConfig<T> extends GenericObjectPoolConfig<T> {

    DefaultObjectPoolConfig(EvictionPolicy<T> evictionPolicy, int maxSize) {

        setEvictionPolicy(evictionPolicy);
        setMaxTotal(maxSize);
        setMaxWaitMillis(0);
        setBlockWhenExhausted(false);
    }
}
