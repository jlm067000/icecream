package com.cognizant.icecream.mock;

import com.cognizant.icecream.pools.api.ResultPool;
import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.MutableResult;
import com.cognizant.icecream.result.MutableServiceResult;
import com.cognizant.icecream.result.ResultFactory;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

public class ResultPoolFactory {

    public static ResultPool createResultPool() {

        ResultPool resultPool = Mockito.mock(ResultPool.class);

        try {
            when(resultPool.createResult(anyBoolean(), any())).then(ResultPoolFactory::mockCreateResult);
        }
        catch(Exception ex) {}

        return resultPool;
    }

    private static MutableResult mockCreateResult(InvocationOnMock iom) {

        boolean success = iom.getArgument(0);
        String message = iom.getArgument(1);

        return ResultFactory.createMutableResult(success, message);
    }
}
