package com.cognizant.icecream.pools.config;

import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.result.MutableServiceResult;
import org.apache.commons.pool2.impl.EvictionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TruckSetResultConfig extends DefaultObjectPoolConfig<MutableServiceResult<Set<Truck>>> {

    @Autowired
    TruckSetResultConfig(
            EvictionPolicy<MutableServiceResult<Set<Truck>>> evictionPolicy,
            @Value("${objectPools.serviceResult.maxSize}") int maxSize
    ) {
        super(evictionPolicy, maxSize);
    }
}
