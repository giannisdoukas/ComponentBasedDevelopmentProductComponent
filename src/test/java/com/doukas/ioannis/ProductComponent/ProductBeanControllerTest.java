package com.doukas.ioannis.ProductComponent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {ProductController.class, AppConfig.class})
class ProductBeanControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ProductController productServiceMock;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productServiceMock).build();
    }

    @Test
    void all() throws Exception {
        ProductBean[] productBean = {
                new ProductBean(UUID.randomUUID().toString(), "description 1", 10.5f),
                new ProductBean(UUID.randomUUID().toString(), "description 2", 11.5f),
        };
        productRepository.deleteAll();
        productRepository.saveAll(Arrays.asList(productBean));
        List<ProductBean> result = new ArrayList<ProductBean>();
        productServiceMock.all().forEach(result::add);

        Assert.assertThat(
                result,
                IsIterableContainingInAnyOrder.containsInAnyOrder(productBean)
        );


        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(
                        jsonPath("$[*].id",
                                containsInAnyOrder(productBean[0].getId(), productBean[1].getId())))
                .andExpect(
                        jsonPath("$[*].description",
                                containsInAnyOrder(productBean[0].getDescription(), productBean[1].getDescription())))
                .andExpect(
                        jsonPath("$[*].price")
                                .value(
                                        containsInAnyOrder(
                                                new Double(productBean[0].getPrice()),
                                                new Double(productBean[1].getPrice())
                                        )
                                )
                )
        ;
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