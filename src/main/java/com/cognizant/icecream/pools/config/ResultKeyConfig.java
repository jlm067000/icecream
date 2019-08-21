package com.cognizant.icecream.pools.config;

import com.cognizant.icecream.pools.PoolKey;
import com.cognizant.icecream.result.ServiceResult;
import org.apache.commons.pool2.impl.EvictionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class ResultKeyConfig extends KeyPoolConfig<ServiceResult> {

    @Autowired
    ResultKeyConfig(EvictionPolicy<PoolKey<ServiceResult>> evictionPolicy) {
        super(evictionPolicy);
    }
}
