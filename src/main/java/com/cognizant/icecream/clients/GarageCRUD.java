package com.cognizant.icecream.clients;

import com.cognizant.icecream.models.Garage;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

@Component
public class GarageCRUD {

    private Set<Garage> garages;

    public GarageCRUD() {

        garages = new HashSet<>();

        Garage garage = new Garage();
        garage.setCode("12");

        garages.add(garage);
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

    private static Garage clone(Garage garage) {

        Garage clone = new Garage();
        clone.setCode(garage.getCode());

        return clone;
    }

}
