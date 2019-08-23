package com.cognizant.icecream.result;

public interface MutableResult extends Result {

    void setSuccess(boolean success);
    void setMessage(String message);
}
