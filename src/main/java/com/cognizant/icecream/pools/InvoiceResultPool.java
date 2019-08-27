package com.cognizant.icecream.pools;

import com.cognizant.icecream.models.Invoice;
import com.cognizant.icecream.pools.api.LocalObjectPool;
import com.cognizant.icecream.pools.config.abandoned.DefaultAbandonedConfig;
import com.cognizant.icecream.result.MutableServiceResult;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvoiceResultPool extends ServiceResultObjectPool<Invoice> implements LocalObjectPool<MutableServiceResult<Invoice>> {

    @Autowired
    InvoiceResultPool(
            PooledObjectFactory<MutableServiceResult<Invoice>> factory,
            GenericObjectPoolConfig<MutableServiceResult<Invoice>> config,
            DefaultAbandonedConfig<MutableServiceResult<Invoice>> abandonedConfig
    ) {
        super(factory, config, abandonedConfig, InvoiceResultPool.class);
    }
}
