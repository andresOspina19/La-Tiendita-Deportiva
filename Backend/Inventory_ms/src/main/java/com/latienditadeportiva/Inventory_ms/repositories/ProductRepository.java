package com.latienditadeportiva.Inventory_ms.repositories;

import com.latienditadeportiva.Inventory_ms.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.domain.Pageable;

public interface ProductRepository extends MongoRepository <Product, String> {
    Page<Product> findByProductNameOrDescriptionIgnoreCaseContaining(String productName, String description, Pageable pageable);
    Page<Product> findByProductNameIgnoreCaseContaining(String productName, Pageable pageable);

}