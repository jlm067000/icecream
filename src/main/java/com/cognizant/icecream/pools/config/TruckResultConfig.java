package com.cognizant.icecream.pools.config;

import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.result.MutableServiceResult;
import org.apache.commons.pool2.impl.EvictionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TruckResultConfig extends DefaultObjectPoolConfig<MutableServiceResult<Truck>> {

    @Autowired
    TruckResultConfig(
            EvictionPolicy<MutableServiceResult<Truck>> evictionPolicy,
            @Value("${objectPools.serviceResult.maxSize}") int maxSize
    ) {
        super(evictionPolicy, maxSize);
    }
}
