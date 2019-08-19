package com.cognizant.icecream.pools;

public interface ServiceResultKey<T> {

    Integer getIndex();
    Class<T> getPayloadType();
}
