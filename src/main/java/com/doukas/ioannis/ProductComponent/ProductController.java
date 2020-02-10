package com.doukas.ioannis.ProductComponent;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    public ProductController(){}

    @GetMapping("/")
    List<ProductBean> all(){
        throw new UnsupportedOperationException("Not yet");
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
