package com.cognizant.icecream.mock;

public class MockFactory {

    public static <T> DefaultAbandonedConfigMock<T> createAbandonedConfig(int timeout) {

        return new DefaultAbandonedConfigMock<>(timeout);
    }
}
