package com.doukas.ioannis.ProductComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
        return repository.save(newProduct);
    }

    @GetMapping("/{id}")
    @Nullable
    Optional<ProductBean> getProduct(@PathVariable String id){
        return repository.findById(id);
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
