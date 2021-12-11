package com.latienditadeportiva.Inventory_ms.repositories;

import com.latienditadeportiva.Inventory_ms.models.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CartRepository extends MongoRepository<Cart, String> {
    List<Cart> findAllByUsernameOrderByCreatedDateDesc(String username);
    List<Cart> deleteByUsername(String username);
    Cart findByUsernameAndProductId(String username, String productId);
}