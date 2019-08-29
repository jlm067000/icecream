package com.cognizant.icecream.util;

import com.cognizant.icecream.result.Result;

public class ResultImpl implements Result {

    private String message;
    private boolean success;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
