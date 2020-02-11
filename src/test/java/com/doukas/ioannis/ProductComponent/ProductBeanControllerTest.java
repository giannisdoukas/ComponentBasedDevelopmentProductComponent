package com.doukas.ioannis.ProductComponent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
        productRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
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
        String description = "new description ...";
        float price = 12.5f;
        ProductBean product = new ProductBean(id, description, price);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(product);

        Assert.assertThat(
                product,
                is(productServiceMock.newProduct(product))
        );

        List<ProductBean> result = new ArrayList<ProductBean>();
        productServiceMock.all().forEach(result::add);

        Assert.assertThat(
                result,
                IsIterableContainingInAnyOrder.containsInAnyOrder(product)
        );
        Assert.assertEquals(result.size(), 1);

        productRepository.deleteAll();

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(id)))
                .andExpect(jsonPath("$.description").value(is(description)))
                .andExpect(jsonPath("$.price").value(is(price), Float.class))
        ;

        result.clear();
        productServiceMock.all().forEach(result::add);

        Assert.assertThat(
                result,
                IsIterableContainingInAnyOrder.containsInAnyOrder(product)
        );
        Assert.assertEquals(result.size(), 1);
    }

    @Test
    void getProduct() throws Exception {
        ProductBean productBean = new ProductBean(
                UUID.randomUUID().toString(),
                "description 1", 10.5f
        );
        productRepository.deleteAll();
        productRepository.save(productBean);

        Assert.assertThat(
                productServiceMock.getProduct(productBean.getId()).get(),
                is(productBean)
        );

        mockMvc.perform(get("/" + productBean.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(productBean.getId())))
                .andExpect(jsonPath("$.description").value(is(productBean.getDescription())))
                .andExpect(jsonPath("$.price").value(is(productBean.getPrice()), Float.class));
    }

    @Test
    void replaceProduct() throws Exception {
        String id = UUID.randomUUID().toString();
        String newId = UUID.randomUUID().toString();
        String description = "new description ...";
        String newDescription = "this is the new description";
        float price = 12.5f;
        ProductBean product = new ProductBean(id, description, price);

        ProductBean newProduct = new ProductBean(id, newDescription, price);
        ProductBean newProductNewID = new ProductBean(newId, newDescription, price);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(newProduct);

        // new Product same ID
        productRepository.save(product);
        Assert.assertThat(
                newProduct,
                is(productServiceMock.replaceProduct(newProduct, id))
        );

        List<ProductBean> result = new ArrayList<ProductBean>();
        productServiceMock.all().forEach(result::add);

        Assert.assertThat(
                result,
                IsIterableContainingInAnyOrder.containsInAnyOrder(newProduct)
        );
        Assert.assertEquals(result.size(), 1);

        productRepository.deleteAll();

        // replace and the ID
        productRepository.save(product);
        Assert.assertThat(
                newProductNewID,
                is(productServiceMock.replaceProduct(newProductNewID, id))
        );

        result.clear();
        productServiceMock.all().forEach(result::add);

        Assert.assertThat(
                result,
                IsIterableContainingInAnyOrder.containsInAnyOrder(newProductNewID)
        );
        Assert.assertEquals(result.size(), 1);

        productRepository.deleteAll();

        // test the rest call
        productRepository.save(product);
        mockMvc.perform(put("/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(id)))
                .andExpect(jsonPath("$.description").value(is(newDescription)))
                .andExpect(jsonPath("$.price").value(is(price), Float.class))
        ;

        result.clear();
        productServiceMock.all().forEach(result::add);

        Assert.assertThat(
                result,
                IsIterableContainingInAnyOrder.containsInAnyOrder(newProduct)
        );
        Assert.assertEquals(result.size(), 1);
    }

    @Test
    void deleteProduct() throws Exception {
        String id = UUID.randomUUID().toString();
        String description = "new description ...";
        float price = 12.5f;
        ProductBean product = new ProductBean(id, description, price);
        productRepository.save(product);

        int[] expectedFound = {1};
        int[] expectedNotFound = {0};
        Assert.assertThat(
                productServiceMock.deleteProduct(id),
                is(expectedFound)
        );

        Assert.assertThat(
                productServiceMock.deleteProduct("404"),
                is(expectedNotFound)
        );

        productRepository.save(product);
        mockMvc.perform(delete("/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").value(is(expectedFound), int[].class))
        ;
    }
}