package com.cognizant.icecream.controllers;


import com.cognizant.icecream.clients.GarageCRUD;
import com.cognizant.icecream.models.Garage;
import com.cognizant.icecream.models.TimeSlot;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GarageControllerIT {

    private static final String BASE_URI = "/icecream/garage/";
    private static final String PERSISTED_CODE = "12";
    private static final String UNPERSISTED_CODE = "11";

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

        assertEquals(200, response.getStatus());
    }

    @Test
    public void testUpdateGarage() throws Exception {


    }

    private static Garage generateGarage(String code) {

        Garage garage = new Garage();
        garage.setCode(code);

        return garage;
    }

    private static MockHttpServletRequestBuilder createPostBuilder(String uri, Garage garage) throws JsonProcessingException {

        byte[] serialized = mapper.writeValueAsBytes(garage);

        return post("/icecream/garage/").contentType(MediaType.APPLICATION_JSON).content(serialized);
    }

    private MockHttpServletResponse performMvcGet(String uri) throws Exception {

        return mvc.perform(get(uri)).andReturn().getResponse();
    }

    private MockHttpServletResponse performMvcRequest(MockHttpServletRequestBuilder builder) throws Exception {

        return mvc.perform(builder).andReturn().getResponse();
    }

}
