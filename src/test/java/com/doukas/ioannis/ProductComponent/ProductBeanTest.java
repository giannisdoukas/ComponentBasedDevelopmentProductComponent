package com.doukas.ioannis.ProductComponent;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductBeanTest {

    @Test
    void getPrice() {
        float expectedPrice = 10.5f;
        String id = UUID.randomUUID().toString();
        String description = "description ...";
        ProductBean product = new ProductBean(id, description, expectedPrice);

        assertEquals(product.getPrice(), expectedPrice);
    }

    @Test
    void getDescription() {
        String expectedDescription = "This is the expected";
        float price = 10f;
        String id = UUID.randomUUID().toString();
        ProductBean product = new ProductBean(id, expectedDescription, price);

        assertEquals(product.getDescription(), expectedDescription);
    }

    @Test
    void getId() {
        String description = "description ...";
        float price = 10f;
        String expectedId = UUID.randomUUID().toString();
        ProductBean product = new ProductBean(expectedId, description, price);

        assertEquals(product.getId(), expectedId);
    }
}