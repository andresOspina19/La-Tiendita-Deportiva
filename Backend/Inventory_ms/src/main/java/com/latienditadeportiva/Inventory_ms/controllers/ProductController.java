package com.latienditadeportiva.Inventory_ms.controllers;

import com.latienditadeportiva.Inventory_ms.exceptions.ProductNotFoundException;
import com.latienditadeportiva.Inventory_ms.models.Product;
import com.latienditadeportiva.Inventory_ms.repositories.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/productsByNameOrWithoutNameOrderedBySales")
    ResponseEntity<Map<String, Object>> getProduct(
            @RequestParam(required = false) String productName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size){

        List<Product> products = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size);
        Page<Product> pageProducts;

        if (productName == null)
            pageProducts = productRepository.findAll(paging);
        else
            pageProducts = productRepository.findByProductNameIgnoreCaseContaining(productName, paging);

        products = pageProducts.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("currentPage", pageProducts.getNumber());
        response.put("totalItems", pageProducts.getTotalElements());
        response.put("totalPages", pageProducts.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/products")
    Product newProduct(@RequestBody Product product){
        return productRepository.save(product);
    }
}
