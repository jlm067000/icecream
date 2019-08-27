package com.cognizant.icecream.pools.config.abandoned;

import org.apache.commons.pool2.impl.AbandonedConfig;

public abstract class DefaultAbandonedConfig<T> extends AbandonedConfig {

    public DefaultAbandonedConfig(int timeout) {

        setLogAbandoned(false);
        setRemoveAbandonedOnBorrow(false);
        setRemoveAbandonedOnMaintenance(true);
        setRemoveAbandonedTimeout(timeout);
        setUseUsageTracking(false);
    }
}
