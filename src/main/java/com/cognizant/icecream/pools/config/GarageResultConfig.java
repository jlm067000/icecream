package com.cognizant.icecream.pools.config;

import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.result.MutableServiceResult;
import org.apache.commons.pool2.impl.EvictionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class GarageResultConfig extends DefaultObjectPoolConfig<MutableServiceResult<Garage>> {

    @Autowired
    GarageResultConfig(
            EvictionPolicy<MutableServiceResult<Garage>> evictionPolicy,
            @Value("${objectPools.serviceResult.maxSize}") int maxSize
    ) {
        super(evictionPolicy, maxSize);
    }
}
