package com.cognizant.icecream.mock;

import com.cognizant.icecream.pools.config.DefaultAbandonedConfig;

public class DefaultAbandonedConfigMock<T> extends DefaultAbandonedConfig<T> {

    public DefaultAbandonedConfigMock(int timeout) {
        super(timeout);
    }
}
