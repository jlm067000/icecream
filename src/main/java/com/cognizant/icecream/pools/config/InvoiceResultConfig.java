package com.cognizant.icecream.pools.config;

import com.cognizant.icecream.models.Invoice;
import com.cognizant.icecream.result.MutableServiceResult;
import org.apache.commons.pool2.impl.EvictionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InvoiceResultConfig extends DefaultObjectPoolConfig<MutableServiceResult<Invoice>> {

    @Autowired
    InvoiceResultConfig(
            EvictionPolicy<MutableServiceResult<Invoice>> evictionPolicy,
            @Value("${objectPools.serviceResult.maxSize}") int maxSize
    ) {
        super(evictionPolicy, maxSize);
    }
}
