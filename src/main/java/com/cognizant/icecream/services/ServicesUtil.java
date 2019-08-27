package com.cognizant.icecream.services;

import com.cognizant.icecream.pools.api.ResultPool;
import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.MutableResult;
import com.cognizant.icecream.result.MutableServiceResult;
import com.cognizant.icecream.result.ResultFactory;
import com.cognizant.icecream.result.ServiceResultProcessor;

import java.util.Optional;

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

    static <T, U> T processOptional(
            Optional<U> optional,
            String formatErrStr,
            String formatArg,
            ServiceResultPool<U> resultPool,
            ServiceResultProcessor<U, T> resultProcessor
    ) {
        MutableServiceResult<U> result = processOptional(optional, formatErrStr, formatArg, resultPool);
        T processed = resultProcessor.apply(result);
        resultPool.returnObject(result);

        return processed;
    }

    private static <T, U> MutableServiceResult<U> processOptional(
            Optional<U> optional,
            String formatErrStr,
            String formatArg,
            ServiceResultPool<U> resultPool
    ) {

        if(optional.isPresent()) {
            return ServicesUtil.createResult(true, null, optional.get(), resultPool);
        }
        else {
            String errMsg = String.format(formatErrStr, formatArg);
            return ServicesUtil.createResult(false, errMsg, null, resultPool);
        }
    }
}
