package com.cognizant.icecream.pools.config;

import com.cognizant.icecream.result.MutableServiceResult;
import org.apache.commons.pool2.impl.EvictionPolicy;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class ResultConfig extends GenericObjectPoolConfig<MutableServiceResult> {

    @Autowired
    ResultConfig(EvictionPolicy<MutableServiceResult> evictionPolicy, @Value("objectPools.serviceResult.maxSize") int maxSize) {

        setEvictionPolicy(evictionPolicy);
        setMaxTotal(maxSize);
    }
}
