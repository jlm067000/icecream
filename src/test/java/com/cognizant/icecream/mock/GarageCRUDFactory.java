package com.cognizant.icecream.mock;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.models.Garage;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GarageCRUDFactory {


    static GarageCRUD createMock(Garage persisted, Garage newGarage) {

        GarageCRUD garageCRUD = Mockito.mock(GarageCRUD.class);
        when(garageCRUD.findByCode(persisted.getCode())).thenReturn(Optional.of(persisted));
        when(garageCRUD.findByCode(newGarage.getCode())).thenReturn(Optional.empty());

        when(garageCRUD.remove(persisted.getCode())).thenReturn(true);
        when(garageCRUD.remove(newGarage.getCode())).thenReturn(false);

        Answer<Optional<Garage>> adder = iom -> mockAdd(iom, persisted, newGarage);
        when(garageCRUD.add(any())).then(adder);

        Answer<Optional<Garage>> updater = iom -> mockUpdate(iom, persisted);
        when(garageCRUD.update(any())).then(updater);

        return garageCRUD;
    }

    private static Optional<Garage> mockAdd(InvocationOnMock iom, Garage persisted, Garage newGarage) {

        Garage garage = iom.getArgument(0);

        if(persisted.equals(garage)) {
            return Optional.of(garage);
        }
        else if(newGarage.equals(garage)) {
            return Optional.of(garage);
        }
        else {
            return Optional.empty();
        }
    }

    private static Optional<Garage> mockUpdate(InvocationOnMock iom, Garage persisted) {

        Garage garage = iom.getArgument(0);

        if(persisted.getCode().equals(garage.getCode())) {
            return Optional.of(garage);
        }

        return Optional.empty();
    }

}
