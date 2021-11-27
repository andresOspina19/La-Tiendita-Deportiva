package com.latienditadeportiva.Inventory_ms.repositories;

import com.latienditadeportiva.Inventory_ms.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository <Product, String> { }