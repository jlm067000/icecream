package com.cognizant.icecream.models;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Garage {

    @NotNull
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Garage garage = (Garage) o;
        return Objects.equals(code, garage.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
