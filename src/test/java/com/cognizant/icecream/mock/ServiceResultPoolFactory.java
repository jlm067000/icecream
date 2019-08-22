package com.cognizant.icecream.mock;

import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.MutableServiceResult;
import com.cognizant.icecream.result.ResultFactory;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

public class ServiceResultPoolFactory {

    public static <T> ServiceResultPool<T> createServiceResultPool() {

        ServiceResultPool<T> resultPool = Mockito.mock(ServiceResultPool.class);

        try {
            when(resultPool.createResult(anyBoolean(), any(), any())).then(ServiceResultPoolFactory::mockCreateServiceResult);
        }
        catch(Exception ex) {}


        return resultPool;
    }


    private static <T> MutableServiceResult<T> mockCreateServiceResult(InvocationOnMock iom) {

        boolean success = iom.getArgument(0);
        String message = iom.getArgument(1);
        T payload = iom.getArgument(2);

        return ResultFactory.createMutableServiceResult(success, message, payload);
    }

}
