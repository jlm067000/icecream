package com.cognizant.icecream.services;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.TruckCRUD;
import com.cognizant.icecream.clients.TruckDeploymentClient;
import com.cognizant.icecream.clients.TruckPurchasingClient;
import com.cognizant.icecream.mock.MockFactory;
import com.cognizant.icecream.models.*;
import com.cognizant.icecream.pools.api.ResultPool;
import com.cognizant.icecream.pools.api.ServiceResultPool;
import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.result.ResultFactory;
import com.cognizant.icecream.result.ResultProcessor;
import com.cognizant.icecream.result.ServiceResult;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TruckServiceTest {

    private static final String ALCOHOLIC_VIN = "13";
    private static final String NONALCOHOLIC_VIN = "12";
    private static final String UNPERSISTED_VIN = "11";
    private static final String GARAGE_CODE = "12";
    private static final String INVALID_CODE = "11";

    private static Truck unpersisted;
    private static Truck invalid;

    private static Garage validGarage;
    private static Garage invalidGarage;

    private static Neighborhood neighborhood;

    private static Object dontcare;

    private TruckPurchaseOrder validOrder;
    private TruckPurchaseOrder existingTrucksOrder;
    private TruckPurchaseOrder invalidGarageOrder;

    private Truck alcoholic;
    private Truck nonalcoholic;

    private GarageCRUD garageCRUD;
    private TruckCRUD truckCRUD;
    private TruckPurchasingClient purchasingClient;
    private TruckDeploymentClient deploymentClient;
    private ResultPool resultPool;
    private ServiceResultPool<Truck> serviceResultPool;
    private ServiceResultPool<Invoice> invoicePool;

    private TruckService truckService;

    @BeforeClass
    public static void init() {

        unpersisted = generateTruck(UNPERSISTED_VIN, true);
        invalid = new Truck();
        validGarage = new Garage();
        validGarage.setCode(GARAGE_CODE);
        invalidGarage = new Garage();
        invalidGarage.setCode(INVALID_CODE);
        neighborhood = new Neighborhood();
        dontcare = new Object();
    }

    @Before
    public void setup() {

        initializeTestData();

        mockPurchasingClient();
        mockDeploymentClient();

        truckCRUD = MockFactory.createTruckCRUD(alcoholic, nonalcoholic, unpersisted);
        resultPool = MockFactory.createResultPool();
        serviceResultPool = MockFactory.createServiceResultPool();
        invoicePool = MockFactory.createServiceResultPool();

        mockGarageCRUD();

        truckService = new TruckService(garageCRUD, truckCRUD, purchasingClient, deploymentClient, resultPool, serviceResultPool, invoicePool);
    }

    private void initializeTestData() {

        alcoholic = generateTruck(ALCOHOLIC_VIN, true);
        nonalcoholic = generateTruck(NONALCOHOLIC_VIN, false);

        initializePurchaseOrders();
    }

    private void initializePurchaseOrders() {

        Set<Truck> newTrucks = new HashSet<>();
        newTrucks.add(unpersisted);

        Set<Truck> existing = new HashSet<>();
        existing.add(alcoholic);
        existing.add(nonalcoholic);

        PaymentDetails details = new PaymentDetails();

        validOrder = new TruckPurchaseOrder();
        validOrder.setGarage(validGarage);
        validOrder.setTrucks(newTrucks);
        validOrder.setPaymentDetails(details);

        invalidGarageOrder = new TruckPurchaseOrder();
        invalidGarageOrder.setGarage(invalidGarage);
        invalidGarageOrder.setTrucks(newTrucks);
        invalidGarageOrder.setPaymentDetails(details);

        existingTrucksOrder = new TruckPurchaseOrder();
        existingTrucksOrder.setGarage(validGarage);
        existingTrucksOrder.setTrucks(existing);
        existingTrucksOrder.setPaymentDetails(details);
    }

    private void mockGarageCRUD() {

        garageCRUD = Mockito.mock(GarageCRUD.class);

        when(garageCRUD.findAllByGarage(any())).then(this::mockGetTrucksByGarage);
        when(garageCRUD.findByCode(GARAGE_CODE)).then(this::mockGetGarage);
    }

    private Optional<Garage> mockGetGarage(InvocationOnMock iom) {

        String garageCode = iom.getArgument(0);

        if(GARAGE_CODE.equals(garageCode)) {
            return Optional.of(validGarage);
        }
        else {
            return Optional.empty();
        }
    }

    private Set<TruckGarage> mockGetTrucksByGarage(InvocationOnMock iom) {

        Garage garage = iom.getArgument(0);

        if(garage == null) {
            return new HashSet<>();
        }
        else if(!GARAGE_CODE.equals(garage.getCode())) {
            return new HashSet<>();
        }

        Set<TruckGarage> garageTrucks = new HashSet<>();

        TruckGarage alcoholicTG = createTruckGarage(garage, alcoholic);
        TruckGarage nonalcoholicTG = createTruckGarage(garage, nonalcoholic);

        garageTrucks.add(alcoholicTG);
        garageTrucks.add(nonalcoholicTG);

        return garageTrucks;
    }

    private void mockPurchasingClient() {

        Invoice invoice = new Invoice();

        purchasingClient = Mockito.mock(TruckPurchasingClient.class);

        when(purchasingClient.purchaseTrucks(validOrder)).thenReturn(Optional.of(invoice));
        when(purchasingClient.purchaseTrucks(invalidGarageOrder)).thenReturn(Optional.empty());
        when(purchasingClient.purchaseTrucks(existingTrucksOrder)).thenReturn(Optional.empty());
    }

    private void mockDeploymentClient() {

        Result result = ResultFactory.createResult(true, "");
        deploymentClient = Mockito.mock(TruckDeploymentClient.class);
        when(deploymentClient.deployTruck(any())).thenReturn(result);
        when(deploymentClient.patrol(true, neighborhood)).thenReturn(true);
        when(deploymentClient.patrol(false, neighborhood)).thenReturn(true);
    }

    @Test
    public void testGetTruck() {

        truckService.getTruck(ALCOHOLIC_VIN, this::testGetPersisted);
        truckService.getTruck(UNPERSISTED_VIN, this::testGetUnpersisted);
    }

    private Object testGetPersisted(ServiceResult<Truck> result) {

        assertNotNull(result);
        assertTrue(result.isSuccess());

        Truck truck = result.getPayload();

        assertEquals(truck.getVin(), ALCOHOLIC_VIN);
        verify(truckCRUD).findByVIN(ALCOHOLIC_VIN);

        return dontcare;
    }

    private Object testGetUnpersisted(ServiceResult<Truck> result) {

        assertNotNull(result);
        assertFalse(result.isSuccess());

        Truck truck = result.getPayload();

        assertNull(truck);
        verify(truckCRUD).findByVIN(UNPERSISTED_VIN);

        return dontcare;
    }

    @Test
    public void testAddTruck() {

        truckService.addTruck(unpersisted, this::testAddUnpersisted);
        truckService.addTruck(invalid, this::testAddInvalid);
    }

    private Object testAddUnpersisted(ServiceResult<Truck> result) {

        assertNotNull(result);
        assertTrue(result.isSuccess());

        Truck added = result.getPayload();

        assertEquals(added, unpersisted);
        verify(truckCRUD).add(unpersisted);

        return dontcare;
    }

    private Object testAddInvalid(ServiceResult<Truck> result) {

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        verify(truckCRUD).add(invalid);

        return dontcare;
    }

    @Test
    public void testUpdateTruck() {

        nonalcoholic.setAlcoholic(true);
        truckService.updateTruck(nonalcoholic, this::testUpdateNonalchoholic);
        truckService.updateTruck(unpersisted, this::testUpdateUnpersisted);
    }

    private Object testUpdateNonalchoholic(ServiceResult<Truck> result) {

        assertNotNull(result);
        assertTrue(result.isSuccess());

        Truck updated = result.getPayload();

        assertEquals(updated, nonalcoholic);
        assertTrue(nonalcoholic.isAlcoholic());
        verify(truckCRUD).update(nonalcoholic);

        return dontcare;
    }

    private Object testUpdateUnpersisted(ServiceResult<Truck> result) {

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        verify(truckCRUD).update(unpersisted);

        return dontcare;
    }

    @Test
    public void testRemoveTruck() {

        truckService.removeTruck(ALCOHOLIC_VIN, this::testRemovePersistedTruck);
        truckService.removeTruck(UNPERSISTED_VIN, this::testRemoveUnpersistedTruck);
    }

    private Object testRemovePersistedTruck(Result result) {

        assertTrue(result.isSuccess());
        verify(truckCRUD).remove(ALCOHOLIC_VIN);

        return dontcare;
    }

    private Object testRemoveUnpersistedTruck(Result result) {

        assertFalse(result.isSuccess());
        verify(truckCRUD).remove(UNPERSISTED_VIN);

        return dontcare;
    }

    @Test
    public void testPurchaseTrucks() {

        truckService.purchaseTrucks("", validOrder, this::testValidPurchaseOrder);
        truckService.purchaseTrucks("", invalidGarageOrder, this::testInvalidPurchaseOrder);
        truckService.purchaseTrucks("", existingTrucksOrder, this::testInvalidPurchaseOrder);
    }

    private Object testValidPurchaseOrder(ServiceResult<Invoice> result) {

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());

        return dontcare;
    }

    private Object testInvalidPurchaseOrder(ServiceResult<Invoice> result) {

        assertNotNull(result);
        assertFalse(result.isSuccess());

        return dontcare;
    }

    @Test
    public void testDeployTruck() {

        TruckGarage truckGarage = new TruckGarage();

        ResultProcessor<Object> resultProcessor = r -> testDeployValidTruckGarage(r, truckGarage);

        truckGarage.setTruck(alcoholic);
        truckGarage.setGarage(validGarage);

        truckService.deploy(truckGarage, resultProcessor);

        resultProcessor = r -> testDeployInvalidTruckGarage(r, truckGarage);

        truckGarage.setTruck(unpersisted);
        truckGarage.setGarage(validGarage);

        truckService.deploy(truckGarage, resultProcessor);

        truckGarage.setTruck(alcoholic);
        truckGarage.setGarage(invalidGarage);

        truckService.deploy(truckGarage, resultProcessor);

        truckGarage.setTruck(unpersisted);
        truckGarage.setGarage(invalidGarage);

        truckService.deploy(truckGarage, resultProcessor);
    }

    private Object testDeployValidTruckGarage(Result result, TruckGarage truckGarage) {

        assertNotNull(result);
        assertTrue(result.isSuccess());
        verify(deploymentClient).deployTruck(truckGarage);

        return dontcare;
    }

    private Object testDeployInvalidTruckGarage(Result result, TruckGarage truckGarage) {

        assertNotNull(result);
        assertFalse(result.isSuccess());

        return dontcare;
    }

    @Test
    public void testPatrol() {

        ResultProcessor<Object> resultProcessor = r -> testPatrol(r, true, neighborhood);
        truckService.patrol(true, neighborhood, resultProcessor);

        resultProcessor = r -> testPatrol(r, false, neighborhood);
        truckService.patrol(false, neighborhood, resultProcessor);
    }

    private Object testPatrol(Result result, boolean isAlcoholic, Neighborhood neighborhood) {

        assertNotNull(result);
        assertTrue(result.isSuccess());
        verify(deploymentClient).patrol(isAlcoholic, neighborhood);

        return dontcare;
    }

    @Test
    public void testGetTrucks() {

        Set<Truck> trucks = truckService.getTrucks();

        assertNotNull(trucks);
        assertEquals(2, trucks.size());
        assertTrue(trucks.contains(alcoholic));
        assertTrue(trucks.contains(nonalcoholic));

        verify(truckCRUD).findAll();
    }

    @Test
    public void testGetTrucksByGarageCode() {

        Set<Truck> trucks = truckService.getTrucks(GARAGE_CODE);

        assertNotNull(trucks);
        assertEquals(2, trucks.size());
        assertTrue(trucks.contains(alcoholic));
        assertTrue(trucks.contains(nonalcoholic));
        verify(garageCRUD).findAllByGarage(any());

        trucks = truckService.getTrucks(INVALID_CODE);
        assertNotNull(trucks);
        assertTrue(trucks.isEmpty());
    }

    @Test
    public void testGetAllByAlcholic() {

        Set<Truck> trucks = truckService.getTrucks(true);

        assertNotNull(trucks);
        assertEquals(1, trucks.size());
        assertTrue(trucks.contains(alcoholic));

        trucks = truckService.getTrucks(false);

        assertNotNull(trucks);
        assertEquals(1, trucks.size());
        assertTrue(trucks.contains(nonalcoholic));
    }

    @Test
    public void testGetAllByCodeAndAlcoholic() {

        Set<Truck> trucks = truckService.getTrucks(GARAGE_CODE,true);

        assertNotNull(trucks);
        assertEquals(1, trucks.size());
        assertTrue(trucks.contains(alcoholic));

        trucks = truckService.getTrucks(GARAGE_CODE,false);

        assertNotNull(trucks);
        assertEquals(1, trucks.size());
        assertTrue(trucks.contains(nonalcoholic));

        trucks = truckService.getTrucks(INVALID_CODE,true);

        assertNotNull(trucks);
        assertTrue(trucks.isEmpty());

        trucks = truckService.getTrucks(INVALID_CODE,false);

        assertNotNull(trucks);
        assertTrue(trucks.isEmpty());
    }

    private static Truck generateTruck(String VIN, boolean alcoholic) {

        Truck truck = new Truck();
        truck.setVin(VIN);
        truck.setAlcoholic(alcoholic);

        return truck;
    }

    private TruckGarage createTruckGarage(Garage garage, Truck truck) {

        TruckGarage truckGarage = new TruckGarage();
        truckGarage.setGarage(garage);
        truckGarage.setTruck(truck);

        return truckGarage;
    }
}
