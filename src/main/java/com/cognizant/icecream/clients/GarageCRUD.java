package com.cognizant.icecream.clients;

import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.Truck;
import com.cognizant.icecream.models.TruckGarage;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class GarageCRUD {

    private Set<Garage> garages;
    private Set<TruckGarage> truckGarages;

    public GarageCRUD() {

        garages = new HashSet<>();

        Garage garage = new Garage();
        garage.setCode("12");

        garages.add(garage);

        truckGarages = new HashSet<>();

        Truck truck = new Truck();
        truck.setVin("1");
        TruckGarage truckGarage = new TruckGarage();
        truckGarage.setTruck(truck);
        truckGarage.setGarage(garage);

        truckGarages.add(truckGarage);
    }

    public Optional<Garage> findByCode(String code) {

        Garage persisted = garages.stream()
                                  .filter(g -> Objects.equals(g.getCode(), code))
                                  .findAny()
                                  .orElse(null);

        if(persisted == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(clone(persisted));
        }
    }

    public Optional<Garage> add(Garage garage) {

        if(garages.contains(garage)) {
            return Optional.empty();
        }

        garages.add(clone(garage));
        return Optional.of(garage);
    }

    public Optional<Garage> update(final Garage garage) {

        Garage persisted = garages.stream()
                                  .filter(g -> Objects.equals(g, garage))
                                  .findAny()
                                  .orElse(null);

        if(persisted == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(garage);
        }
    }

    public boolean remove(final String code) {

        Predicate<Garage> condition = g -> Objects.equals(code, g.getCode());
        return garages.removeIf(condition);
    }

    public Set<TruckGarage> findAllByGarage(Garage garage) {

        return truckGarages.stream()
                           .filter(tg -> Objects.equals(tg.getGarage(), garage))
                           .map(GarageCRUD::clone)
                           .collect(Collectors.toSet());
    }

    private static Garage clone(Garage garage) {

        Garage clone = new Garage();
        clone.setCode(garage.getCode());

        return clone;
    }

    private static Truck clone(Truck truck) {

        Truck clone = new Truck();
        clone.setAlcoholic(truck.isAlcoholic());
        clone.setVin(truck.getVin());

        return clone;
    }

    private static TruckGarage clone(TruckGarage truckGarage) {

        TruckGarage clone = new TruckGarage();
        clone.setGarage(clone(truckGarage.getGarage()));
        clone.setTruck(clone(truckGarage.getTruck()));

        return clone;
    }
}
