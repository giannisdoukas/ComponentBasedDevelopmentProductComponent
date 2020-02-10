package com.doukas.ioannis.ProductComponent;

import com.doukas.ioannis.ProductComponent.Product;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@RestController
public class ProductController {
    @GetMapping("/")
    List<Product> all(){
        throw new UnsupportedOperationException("Not yet");
    }

    @PostMapping("/")
    Product newProduct(@RequestBody Product newProduct){
        throw new UnsupportedOperationException("Not yet");
    }

    @GetMapping("/{id}")
    Product getProduct(@PathVariable Long id){
        throw new UnsupportedOperationException("Not yet");
    }

    @PutMapping("/{id}")
    Product replaceProduct(@RequestBody Product newProduct, @PathVariable Long id){
        throw new UnsupportedOperationException("Not yet");
    }

    @DeleteMapping("/{id}")
    int deleteProduct(@PathVariable Long id){
        throw new UnsupportedOperationException("Not yet");
    }

}
