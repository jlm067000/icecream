package com.cognizant.icecream.models;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Truck {

    @NotNull
    private String vin;

    @NotNull
    private boolean alcoholic;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public boolean isAlcoholic() {
        return alcoholic;
    }

    public void setAlcoholic(boolean alcoholic) {
        this.alcoholic = alcoholic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Truck truck = (Truck) o;
        return alcoholic == truck.alcoholic &&
                Objects.equals(vin, truck.vin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vin, alcoholic);
    }
}
