package com.latienditadeportiva.Inventory_ms.repositories;

import com.latienditadeportiva.Inventory_ms.models.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrdersRepository extends MongoRepository<Orders, String> {
    List<Orders> findAllByUsernameOrderByCreatedDateDesc(String username);
}
