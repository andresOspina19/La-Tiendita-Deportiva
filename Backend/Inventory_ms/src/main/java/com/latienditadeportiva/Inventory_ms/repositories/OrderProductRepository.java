package com.latienditadeportiva.Inventory_ms.repositories;

import com.latienditadeportiva.Inventory_ms.models.OrderProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderProductRepository extends MongoRepository<OrderProduct, String> { }