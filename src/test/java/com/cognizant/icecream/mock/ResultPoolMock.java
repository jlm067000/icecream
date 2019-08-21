package com.cognizant.icecream.mock;

import com.cognizant.icecream.pools.PoolKey;
import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.ServiceResult;
import org.mockito.Mockito;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.mockito.Mockito.when;

class ResultPoolMock implements ServiceResultPool {

    private ServiceResult result;
    private PoolKey<ServiceResult> key;

    ResultPoolMock() {

        result = Mockito.mock(ServiceResult.class);
        key = Mockito.mock(PoolKey.class);
    }

    @Override
    public <V> PoolKey<ServiceResult> createResult(boolean success, String message, V payload) {

        when(result.isSuccess()).thenReturn(success);
        when(result.getPayload()).thenReturn(payload);
        when(result.getMessage()).thenReturn(message);

        return key;
    }

    @Override
    public void processObject(PoolKey<ServiceResult> key, Consumer<ServiceResult> objectProcessor) {

        objectProcessor.accept(result);
    }

    @Override
    public <V> V processObject(PoolKey<ServiceResult> key, Function<ServiceResult, V> objectProcessor) {

        return objectProcessor.apply(result);
    }
}
