package com.doukas.ioannis.ProductComponent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.doukas.ioannis.ProductComponent")
public class TestConfig {

    @Bean
    public ProductController productController(){
        return new ProductController();
    }

}
