package com.cognizant.icecream.clients;

import com.cognizant.icecream.models.Truck;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class TruckCRUD {

    private Set<Truck> trucks;

    public TruckCRUD() {

        trucks = new HashSet<>();

        Truck truck = new Truck();
        truck.setVin("1");

        trucks.add(truck);
    }

    public Optional<Truck> findByVIN(String vin) {

        Truck persisted = trucks.stream()
                                .filter(t -> Objects.equals(vin, t.getVin()))
                                .findAny()
                                .orElse(null);

        if(persisted == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(clone(persisted));
        }
    }

    public Optional<Truck> add(Truck truck) {

        if(trucks.contains(truck)) {
            return Optional.empty();
        }

        trucks.add(clone(truck));

        return Optional.of(truck);
    }

    public Optional<Truck> update(final Truck truck) {

        Truck persisted = trucks.stream()
                                .filter(t -> Objects.equals(truck, t))
                                .findAny()
                                .orElse(null);

        if(persisted == null) {
            return Optional.empty();
        }

        trucks.remove(persisted);
        trucks.add(clone(truck));

        return Optional.of(truck);
    }

    public boolean remove(String vin) {

        Predicate<Truck> condition = t -> Objects.equals(t.getVin(), vin);
        return trucks.removeIf(condition);
    }

    public Set<Truck> findAll() {

        return trucks.stream()
                     .map(TruckCRUD::clone)
                     .collect(Collectors.toSet());
    }

    private static Truck clone(Truck truck) {

        Truck clone = new Truck();
        clone.setVin(truck.getVin());
        clone.setAlcoholic(truck.isAlcoholic());

        return clone;
    }

}
