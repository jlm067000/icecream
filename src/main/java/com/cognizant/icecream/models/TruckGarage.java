package com.cognizant.icecream.models;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class TruckGarage {

    @Valid
    @NotNull
    private Truck truck;

    @Valid
    @NotNull
    private Garage garage;

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public Garage getGarage() {
        return garage;
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckGarage that = (TruckGarage) o;
        return Objects.equals(truck, that.truck) &&
                Objects.equals(garage, that.garage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(truck, garage);
    }
}
