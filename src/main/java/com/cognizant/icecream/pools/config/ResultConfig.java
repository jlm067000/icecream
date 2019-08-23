package com.cognizant.icecream.pools.config;

import com.cognizant.icecream.result.MutableResult;
import org.apache.commons.pool2.impl.EvictionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResultConfig extends DefaultObjectPoolConfig<MutableResult> {

    @Autowired
    ResultConfig(EvictionPolicy<MutableResult> evictionPolicy, @Value("${objectPools.result.maxSize}") int maxSize) {
        super(evictionPolicy, maxSize);
    }
}
