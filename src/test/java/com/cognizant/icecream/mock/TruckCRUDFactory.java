package com.cognizant.icecream.mock;

import com.cognizant.icecream.clients.TruckCRUD;
import com.cognizant.icecream.models.Truck;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TruckCRUDFactory {

    static TruckCRUD createMock(Truck alcoholic, Truck nonalcoholic, Truck newTruck) {

        TruckCRUD truckCRUD = Mockito.mock(TruckCRUD.class);
        when(truckCRUD.findByVIN(alcoholic.getVin())).thenReturn(Optional.of(alcoholic));
        when(truckCRUD.findByVIN(nonalcoholic.getVin())).thenReturn(Optional.of(alcoholic));
        when(truckCRUD.findByVIN(newTruck.getVin())).thenReturn(Optional.empty());

        when(truckCRUD.remove(alcoholic.getVin())).thenReturn(true);
        when(truckCRUD.remove(nonalcoholic.getVin())).thenReturn(true);
        when(truckCRUD.remove(newTruck.getVin())).thenReturn(false);

        Answer<Optional<Truck>> adder = iom -> mockAdd(iom, alcoholic, nonalcoholic, newTruck);
        when(truckCRUD.add(any())).then(adder);

        Answer<Optional<Truck>> updater = iom -> mockUpdate(iom, alcoholic, nonalcoholic);
        when(truckCRUD.update(any())).then(updater);

        Answer<Set<Truck>> collector = iom -> mockFindAll(iom, alcoholic, nonalcoholic);
        when(truckCRUD.findAll()).then(collector);

        return truckCRUD;
    }

    private static Optional<Truck> mockAdd(InvocationOnMock iom, Truck alcoholic, Truck nonalcoholic, Truck newTruck) {

        Truck truck = iom.getArgument(0);

        if(alcoholic.equals(truck) || nonalcoholic.equals(truck) || newTruck.equals(truck)) {
            return Optional.of(truck);
        }
        else {
            return Optional.empty();
        }
    }

    private static Optional<Truck> mockUpdate(InvocationOnMock iom, Truck alcholic, Truck nonalcoholic) {

        Truck truck = iom.getArgument(0);
        String vin = truck.getVin();

        if(alcholic.getVin().equals(vin) || nonalcoholic.getVin().equals(vin)) {
            return Optional.of(truck);
        }

        return Optional.empty();
    }

    private static Set<Truck> mockFindAll(InvocationOnMock iom, Truck alcoholic, Truck nonalcoholic) {

        Set<Truck> trucks = new HashSet<>();
        trucks.add(alcoholic);
        trucks.add(nonalcoholic);

        return trucks;
    }

}
