package com.doukas.ioannis.ProductComponent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, AppConfig.class})
@WebAppConfiguration
class ProductBeanControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ProductController productServiceMock;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductController()).build();
    }

    @Test
    void all() throws Exception {
        mockMvc.perform(
                get("/")
        ).andExpect(status().isOk());
    }

    @Test
    void newProduct() throws Exception {
        String id = UUID.randomUUID().toString();
        String description = "description ...";
        float price = 10f;
        ProductBean product = new ProductBean(id, description, price);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(product);

        mockMvc.perform(
                post("/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(requestJson)
        )
                .andExpect(status().isOk());
    }

    @Test
    void getProduct() throws Exception {
        mockMvc.perform(
                get("/1")
        ).andExpect(status().isOk());
    }

    @Test
    void replaceProduct() throws Exception {
        String id = UUID.randomUUID().toString();
        String description = "description ...";
        float price = 10f;
        ProductBean product = new ProductBean(id, description, price);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(product);

        mockMvc.perform(
                put("/" + id)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(requestJson)
        ).andExpect(status().isOk());
    }

    @Test
    void deleteProduct() throws Exception {
        mockMvc.perform(
                delete("/1")
        ).andExpect(status().isOk());
    }
}