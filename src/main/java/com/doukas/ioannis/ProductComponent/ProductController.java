package com.doukas.ioannis.ProductComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@RestController
public class ProductController {

    @Autowired
    ProductRepository repository;

    public ProductController(){}

    @GetMapping("/")
    Iterable<ProductBean> all() {
        return repository.findAll();
    }

    @PostMapping("/")
    ProductBean newProduct(@RequestBody ProductBean newProduct){
        throw new UnsupportedOperationException("Not yet");
    }

    @GetMapping("/{id}")
    ProductBean getProduct(@PathVariable Long id){
        throw new UnsupportedOperationException("Not yet");
    }

    @PutMapping("/{id}")
    ProductBean replaceProduct(@RequestBody ProductBean newProduct, @PathVariable Long id){
        throw new UnsupportedOperationException("Not yet");
    }

    /**
     *
     * @param id
     * @return Number of deleted products. That should be 0 or 1
     */
    @DeleteMapping("/{id}")
    int deleteProduct(@PathVariable Long id){
        throw new UnsupportedOperationException("Not yet");
    }

}
