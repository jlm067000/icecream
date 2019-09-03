package com.cognizant.icecream.controllers;


import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import com.cognizant.icecream.result.Result;
import com.cognizant.icecream.util.ResultImpl;
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

import java.time.LocalDate;

import static com.cognizant.icecream.util.MockMvcUtil.createPostBuilder;
import static com.cognizant.icecream.util.MockMvcUtil.createPutBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GarageControllerIT {

    private static final String BASE_URI = "/icecream/garage/";
    private static final String PERSISTED_CODE = "12";
    private static final String UNPERSISTED_CODE = "11";
    private static final String AUTHORIZATION = "This is a highly secure authorization header.";

    private static ObjectMapper mapper;

    private static Garage persisted;
    private static Garage newGarage;
    private static Garage invalidGarage;

    private static TimeSlot futureTime;
    private static TimeSlot pastTime;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GarageCRUD garageCRUD;

    @BeforeClass
    public static void init() {

        mapper = new ObjectMapper();

        initializeTestData();
    }

    private static void initializeTestData() {

        persisted = generateGarage(PERSISTED_CODE);
        newGarage = generateGarage(UNPERSISTED_CODE);
        invalidGarage = new Garage();

        LocalDate future = LocalDate.now().plusDays(1);
        LocalDate past = LocalDate.now().minusDays(1);

        futureTime = new TimeSlot();
        futureTime.setHour(1);
        futureTime.setDate(future);

        pastTime = new TimeSlot();
        pastTime.setHour(1);
        pastTime.setDate(past);
    }

    @Before
    public void setup() {
        garageCRUD.add(persisted);
        garageCRUD.remove(UNPERSISTED_CODE);
    }


    @Test
    public void testGetGarage() throws Exception {

        MockHttpServletResponse response = performMvcGet(BASE_URI + PERSISTED_CODE);

        assertEquals(200, response.getStatus());

        Garage resultBody = mapper.readValue(response.getContentAsString(), Garage.class);
        assertEquals(persisted, resultBody);

        response = performMvcGet(BASE_URI + UNPERSISTED_CODE);

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testAddGarage() throws Exception {

        MockHttpServletRequestBuilder builder = createPostBuilder(BASE_URI, newGarage);
        MockHttpServletResponse response = performMvcRequest(builder);

        assertEquals(201, response.getStatus());

        Garage resultBody = mapper.readValue(response.getContentAsString(), Garage.class);
        assertEquals(newGarage, resultBody);

        response = performMvcGet(BASE_URI + UNPERSISTED_CODE);

        assertEquals(200, response.getStatus());

        resultBody = mapper.readValue(response.getContentAsString(), Garage.class);

        assertEquals(newGarage, resultBody);

        builder = createPostBuilder(BASE_URI, invalidGarage);
        response = performMvcRequest(builder);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testUpdateGarage() throws Exception {

        Garage current = new Garage();
        current.setCode(PERSISTED_CODE);

        MockHttpServletRequestBuilder builder = createPutBuilder(BASE_URI + PERSISTED_CODE, current);
        MockHttpServletResponse response = performMvcRequest(builder);

        assertEquals(200, response.getStatus());

        Garage resultBody = mapper.readValue(response.getContentAsString(), Garage.class);

        assertEquals(persisted, resultBody);

        builder = createPutBuilder(BASE_URI + UNPERSISTED_CODE, newGarage);
        response = performMvcRequest(builder);

        assertEquals(400, response.getStatus());

        builder = createPutBuilder(BASE_URI + UNPERSISTED_CODE, persisted);
        response = performMvcRequest(builder);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testRemoveGarage() throws Exception {

        MockHttpServletResponse response = performMvcRequest(delete(BASE_URI + PERSISTED_CODE));

        assertEquals(200, response.getStatus());

        response = performMvcRequest(get(BASE_URI + PERSISTED_CODE));

        assertEquals(404, response.getStatus());

        response = performMvcRequest(delete(BASE_URI + UNPERSISTED_CODE));

        assertEquals(204, response.getStatus());
    }

    @Test
    public void testResupply() throws Exception {

        SerializableTimeSlot serializable = new SerializableTimeSlot(futureTime);
        MockHttpServletRequestBuilder builder = createPostBuilder(BASE_URI + PERSISTED_CODE + "/resupply", serializable);
        builder.header("Authorization", AUTHORIZATION);

        MockHttpServletResponse response = performMvcRequest(builder);

        assertEquals(200, response.getStatus());

        builder = createPostBuilder(BASE_URI + UNPERSISTED_CODE + "/resupply", serializable);
        builder.header("Authorization", AUTHORIZATION);
        response = performMvcRequest(builder);

        assertEquals(400, response.getStatus());

        Result responseBody = mapper.readValue(response.getContentAsString(), ResultImpl.class);

        assertFalse(responseBody.isSuccess());

        serializable = new SerializableTimeSlot(pastTime);

        builder = createPostBuilder(BASE_URI + UNPERSISTED_CODE + "/resupply", serializable);
        builder.header("Authorization", AUTHORIZATION);
        response = performMvcRequest(builder);

        assertEquals(400, response.getStatus());

        responseBody = mapper.readValue(response.getContentAsString(), ResultImpl.class);

        assertFalse(responseBody.isSuccess());
    }

    private static Garage generateGarage(String code) {

        Garage garage = new Garage();
        garage.setCode(code);

        return garage;
    }

    private MockHttpServletResponse performMvcGet(String uri) throws Exception {

        return mvc.perform(get(uri)).andReturn().getResponse();
    }

    private MockHttpServletResponse performMvcRequest(MockHttpServletRequestBuilder builder) throws Exception {

        return mvc.perform(builder).andReturn().getResponse();
    }

    private static class SerializableTimeSlot {

        private String date;
        private int hour;

        SerializableTimeSlot(TimeSlot timeSlot) {

            this.date = timeSlot.getDate().toString();
            this.hour = timeSlot.getHour();
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }
    }
}
