package com.cognizant.icecream.pools;

public class PoolCapacityException extends Exception {

    private static final String MESSAGE = "The capacity of %d has been reached for pool %s.";

    public PoolCapacityException(int maxSize, Class<?> poolObjectType) {

        super(String.format(MESSAGE, maxSize, poolObjectType.getName()));
    }
}
