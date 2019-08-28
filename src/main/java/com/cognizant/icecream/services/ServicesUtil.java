package com.cognizant.icecream.services;

import com.cognizant.icecream.pools.api.ResultPool;
import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.MutableResult;
import com.cognizant.icecream.result.MutableServiceResult;
import com.cognizant.icecream.result.ResultFactory;
import com.cognizant.icecream.result.ServiceResultProcessor;

import java.util.Optional;
import java.util.function.Supplier;

class ServicesUtil {

    static <T> MutableServiceResult<T> createResult(boolean success, String message, T payload, ServiceResultPool<T> resultPool)
    {
        try {
            return resultPool.createResult(success, message, payload);
        }
        catch(Exception ex) {
            return ResultFactory.createMutableServiceResult(success, message, payload);
        }
    }

    static MutableResult createResult(boolean success, String message, ResultPool resultPool)
    {
        try {
            return resultPool.createResult(success, message);
        }
        catch(Exception ex) {
            return ResultFactory.createMutableResult(success, message);
        }
    }

    static <T,U> T processOptional(
            Optional<U> optional,
            String errMsg,
            ServiceResultPool<U> pool,
            ServiceResultProcessor<U,T> resultProcessor
    ) {
        MutableServiceResult<U> result = processOptional(optional, errMsg, pool);

        return processResult(result, pool, resultProcessor);
    }

    private static <U> MutableServiceResult<U> processOptional(Optional<U> optional, String errMsg, ServiceResultPool<U> pool) {

        if(optional.isPresent()) {
            return createResult(true, null, optional.get(), pool);
        }
        else {
            return createResult(false, errMsg, null, pool);
        }
    }

    static <T,U> T processOptional(
            Optional<U> optional,
            String formatErrStr,
            Object formatArg,
            ServiceResultPool<U> pool,
            ServiceResultProcessor<U,T> resultProcessor
    ) {
        MutableServiceResult<U> result = processOptional(optional, formatErrStr, formatArg, pool);

        return processResult(result, pool, resultProcessor);
    }

    private static <U> MutableServiceResult<U> processOptional(
            Optional<U> optional,
            String formatErrStr,
            Object formatArg,
            ServiceResultPool<U> pool
    ) {

        if(optional.isPresent()) {
            return createResult(true, null, optional.get(), pool);
        }
        else {
            String errMsg = String.format(formatErrStr, formatArg);
            return createResult(false, errMsg, null, pool);
        }
    }

    private static <T,U> T processResult(
            MutableServiceResult<U> result,
            ServiceResultPool<U> pool,
            ServiceResultProcessor<U,T> resultProcessor
    ) {

        T processed = resultProcessor.apply(result);
        pool.returnObject(result);

        return processed;
    }
}
