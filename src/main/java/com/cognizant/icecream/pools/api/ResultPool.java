package com.cognizant.icecream.pools.api;

import com.cognizant.icecream.pools.PoolCapacityException;
import com.cognizant.icecream.result.MutableResult;

public interface ResultPool extends LocalObjectPool<MutableResult> {

    MutableResult createResult(boolean success, String message) throws PoolCapacityException, Exception;
}
