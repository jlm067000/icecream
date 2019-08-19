package com.cognizant.icecream.clients;

import com.cognizant.icecream.models.result.ClientResult;

public class ClientResultFactory {

    public static ClientResult createResult(boolean success, String message) {

        return new ResultObject(success, message);
    }
}
