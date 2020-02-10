package com.doukas.ioannis.ProductComponent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductBeanTest {

    @Test
    void getPrice() {
        float expectedPrice = 10.5f;
        ProductBean.ProductBeanBuilder builder = new ProductBean.ProductBeanBuilder();
        builder.setPrice(expectedPrice);
        ProductBean product = builder.build();

        assertEquals(product.getPrice(), expectedPrice);
    }

    @Test
    void getDescription() {
        String expectedDescription = "This is the expected";
        ProductBean.ProductBeanBuilder builder = new ProductBean.ProductBeanBuilder();
        builder.setDescription(expectedDescription);
        ProductBean product = builder.build();

        assertEquals(product.getDescription(), expectedDescription);
    }

    @Test
    void getId() {
        Long expectedId = 1L;
        ProductBean.ProductBeanBuilder builder = new ProductBean.ProductBeanBuilder();
        builder.setId(expectedId);
        ProductBean product = builder.build();

        assertEquals(product.getId(), expectedId);
    }
}