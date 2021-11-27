package com.latienditadeportiva.Inventory_ms.controllers;

import com.latienditadeportiva.Inventory_ms.exceptions.ProductNotFoundException;
import com.latienditadeportiva.Inventory_ms.models.Product;
import com.latienditadeportiva.Inventory_ms.repositories.ProductRepository;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products/{productId}")
    Product getProduct(@PathVariable String productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("No se encontró un producto con ID:" + productId));
    }

    @PostMapping("/products")
    Product newProduct(@RequestBody Product product){
        return productRepository.save(product);
    }

    
}
