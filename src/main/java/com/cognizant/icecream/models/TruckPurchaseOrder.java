package com.cognizant.icecream.models;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

public class TruckPurchaseOrder {

    @NotNull
    private Set<@Valid Truck> trucks;

    @Valid
    @NotNull
    private Garage garage;

    private PaymentDetails paymentDetails;


    public Set<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(Set<Truck> trucks) {
        this.trucks = trucks;
    }

    public Garage getGarage() {
        return garage;
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
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
                Objects.equals(garage, that.garage) &&
                Objects.equals(paymentDetails, that.paymentDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trucks, garage, paymentDetails);
    }
}
