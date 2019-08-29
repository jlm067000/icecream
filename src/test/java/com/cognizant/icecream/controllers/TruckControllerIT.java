package com.cognizant.icecream.controllers;

import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.clients.TruckCRUD;
import com.cognizant.icecream.models.*;
import com.cognizant.icecream.util.MockMvcUtil;
import com.cognizant.icecream.util.TruckFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static com.cognizant.icecream.util.MockMvcUtil.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TruckControllerIT {

    private static final String BASE_URI = "/icecream/truck/";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String ALCOHOLIC_VIN = "13";
    private static final String NONALCOHOLIC_VIN = "12";
    private static final String UNPERSISTED_VIN = "11";
    private static final String GARAGE_CODE = "12";
    private static final String INVALID_CODE = "11";

    private static Truck unpersisted;
    private static Truck invalid;
    private static Truck alcoholic;
    private static Truck nonalcoholic;

    private static Garage validGarage;
    private static Garage invalidGarage;

    private static Neighborhood neighborhood;

    private static TruckPurchaseOrder validOrder;
    private static TruckPurchaseOrder existingTrucksOrder;
    private static TruckPurchaseOrder invalidGarageOrder;

    private static Object dontcare;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TruckCRUD truckCRUD;

    @Autowired
    private GarageCRUD garageCRUD;

    @BeforeClass
    public static void init() {

        alcoholic = TruckFactory.createTruck(true, ALCOHOLIC_VIN);
        nonalcoholic = TruckFactory.createTruck(false, NONALCOHOLIC_VIN);
        unpersisted = TruckFactory.createTruck(true, UNPERSISTED_VIN);
        invalid = new Truck();
        validGarage = new Garage();
        validGarage.setCode(GARAGE_CODE);
        invalidGarage = new Garage();
        invalidGarage.setCode(INVALID_CODE);
        neighborhood = new Neighborhood();
        dontcare = new Object();

        initializePurchaseOrders();
    }

    @Before
    public void setup() {

        truckCRUD.add(alcoholic);
        truckCRUD.add(nonalcoholic);
        truckCRUD.remove(UNPERSISTED_VIN);

        garageCRUD.add(validGarage);
        garageCRUD.remove(UNPERSISTED_VIN);
    }

    private static void initializePurchaseOrders() {

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

    @Test
    public void testGetTruck() throws Exception {

        MockHttpServletResponse response = mvc.perform(get(BASE_URI + ALCOHOLIC_VIN)).andReturn().getResponse();

        assertEquals(200, response.getStatus());

        Truck responseBody = MAPPER.readValue(response.getContentAsString(), Truck.class);

        assertEquals(alcoholic, responseBody);

        response = mvc.perform(get(BASE_URI + UNPERSISTED_VIN)).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testAddTruck() throws Exception {

        MockHttpServletRequestBuilder builder = createPostBuilder(BASE_URI, unpersisted);
        MockHttpServletResponse response = mvc.perform(builder).andReturn().getResponse();

        Truck responseBody = MAPPER.readValue(response.getContentAsString(), Truck.class);

        assertEquals(unpersisted, responseBody);

        response = mvc.perform(get(BASE_URI + UNPERSISTED_VIN)).andReturn().getResponse();

        assertEquals(200, response.getStatus());

        responseBody = MAPPER.readValue(response.getContentAsString(), Truck.class);

        assertEquals(unpersisted, responseBody);

        builder = createPostBuilder(BASE_URI, invalid);
        response = mvc.perform(builder).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testUpdateTruck() throws Exception {

        Truck update = TruckFactory.createTruck(true, nonalcoholic.getVin());

        MockHttpServletRequestBuilder builder = createPutBuilder(BASE_URI + NONALCOHOLIC_VIN, update);
        MockHttpServletResponse response = mvc.perform(builder).andReturn().getResponse();

        assertEquals(200, response.getStatus());

        Truck responseBody = MAPPER.readValue(response.getContentAsString(), Truck.class);

        assertEquals(update, responseBody);

        response = mvc.perform(get(BASE_URI + NONALCOHOLIC_VIN)).andReturn().getResponse();

        assertEquals(200, response.getStatus());

        responseBody = MAPPER.readValue(response.getContentAsString(), Truck.class);

        assertEquals(update, responseBody);

        builder = createPutBuilder(BASE_URI + ALCOHOLIC_VIN, nonalcoholic);
        response = mvc.perform(builder).andReturn().getResponse();

        assertEquals(400, response.getStatus());

        builder = createPutBuilder(BASE_URI + UNPERSISTED_VIN, unpersisted);
        response = mvc.perform(builder).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testRemoveTruck() throws Exception {

        MockHttpServletResponse response = mvc.perform(delete(BASE_URI + ALCOHOLIC_VIN)).andReturn().getResponse();

        assertEquals(200, response.getStatus());

        response = mvc.perform(get(BASE_URI + ALCOHOLIC_VIN)).andReturn().getResponse();

        assertEquals(404, response.getStatus());

        response = mvc.perform(delete(BASE_URI + UNPERSISTED_VIN)).andReturn().getResponse();

        assertEquals(204, response.getStatus());
    }

    @Test
    public void testGetTrucks() throws Exception {

        MockHttpServletResponse response = mvc.perform(get(BASE_URI)).andReturn().getResponse();

        assertEquals(200, response.getStatus());

        Set<Truck> responseBody = MAPPER.readValue(response.getContentAsString(), Set.class);

        assertNotNull(responseBody);
    }
}
