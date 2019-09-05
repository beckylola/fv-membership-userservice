package com.nus.ijuice.repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nus.ijuice.FVMembershipApplication;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FVMembershipApplication.class)
@WebAppConfiguration
public abstract class AbstractApplicationTest {

    private static Logger logger = LoggerFactory.getLogger(AbstractApplicationTest.class);


    protected MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    protected void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }


    protected MvcResult performPostRequest(String uri, Object mockRequest, ResultMatcher expectedStatus) throws Exception {
        logger.info("URI ::: " + uri);
        logger.info("mockRequest", mockRequest);
        return performRequest(MockMvcRequestBuilders.post(uri), mockRequest, expectedStatus);
    }

    protected MvcResult performGetRequest(String uri, Object mockRequest, ResultMatcher expectedStatus) throws Exception {
        return performRequest(MockMvcRequestBuilders.get(uri), mockRequest, expectedStatus);
    }

    private MvcResult performRequest(MockHttpServletRequestBuilder requestBuilder,
                                     Object mockRequest, ResultMatcher expectedStatus) throws Exception {

        return this.mvc.perform(requestBuilder
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(mockRequest)))
                .andExpect(expectedStatus).andReturn();
    }
}