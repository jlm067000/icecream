package com.cognizant.icecream.models;

import java.util.Objects;

public class TruckGarage {

    private Truck truck;
    private Garage location;

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public Garage getLocation() {
        return location;
    }

    public void setLocation(Garage location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckGarage that = (TruckGarage) o;
        return Objects.equals(truck, that.truck) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(truck, location);
    }
}
