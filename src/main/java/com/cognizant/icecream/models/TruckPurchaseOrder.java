package com.cognizant.icecream.models;

import java.util.Objects;
import java.util.Set;

public class TruckPurchaseOrder {

    private Set<Truck> trucks;
    private Garage location;
    private PaymentDetails paymentDetails;


    public Set<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(Set<Truck> trucks) {
        this.trucks = trucks;
    }

    public Garage getLocation() {
        return location;
    }

    public void setLocation(Garage location) {
        this.location = location;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckPurchaseOrder that = (TruckPurchaseOrder) o;
        return Objects.equals(trucks, that.trucks) &&
                Objects.equals(location, that.location) &&
                Objects.equals(paymentDetails, that.paymentDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trucks, location, paymentDetails);
    }
}
