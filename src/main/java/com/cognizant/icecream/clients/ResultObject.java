package com.cognizant.icecream.clients;

import java.util.Objects;

public class ResultObject implements Result {

    private boolean success;
    private String message;

    public ResultObject() {}

    public ResultObject(boolean success, String message) {

        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultObject result = (ResultObject) o;
        return success == result.success &&
                Objects.equals(message, result.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, message);
    }
}
