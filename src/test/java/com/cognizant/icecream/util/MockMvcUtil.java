package com.cognizant.icecream.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class MockMvcUtil {

    private static final ObjectMapper MAPPER;
    private static final String AUTHORIZATION = "This is a highly secure authorization header.";

    static {
        MAPPER = new ObjectMapper();
    }

    public static MockHttpServletResponse performGetWithAuthorization(MockMvc mvc, String uri) throws Exception {

        MockHttpServletRequestBuilder builder = get(uri).header("Authorization", AUTHORIZATION);

        return mvc.perform(builder).andReturn().getResponse();
    }

    public static <T> MockHttpServletRequestBuilder createPostBuilder(String uri, T object)
            throws JsonProcessingException
    {

        return populateContent(post(uri), object);
    }

    public static <T> MockHttpServletRequestBuilder createPutBuilder(String uri, T object) throws JsonProcessingException {

        return populateContent(put(uri), object);
    }

    public static <T> MockHttpServletRequestBuilder populateContent(MockHttpServletRequestBuilder builder, T object)
            throws JsonProcessingException
    {
        byte[] serialized = MAPPER.writeValueAsBytes(object);

        return builder.contentType(MediaType.APPLICATION_JSON).content(serialized);
    }
}
