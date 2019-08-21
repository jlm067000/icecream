package com.cognizant.icecream.pools.config;

import com.cognizant.icecream.pools.PoolKey;
import com.cognizant.icecream.result.ServiceResult;
import org.apache.commons.pool2.impl.EvictionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class ServiceResultKeyConfig extends KeyPoolConfig<ServiceResult> {

    @Autowired
    ServiceResultKeyConfig(EvictionPolicy<PoolKey<ServiceResult>> evictionPolicy) {
        super(evictionPolicy);
    }
}
