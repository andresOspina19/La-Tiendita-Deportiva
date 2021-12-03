package com.latienditadeportiva.Inventory_ms.controllers;

import com.latienditadeportiva.Inventory_ms.exceptions.ProductNotFoundException;
import com.latienditadeportiva.Inventory_ms.exceptions.NoStockException;
import com.latienditadeportiva.Inventory_ms.models.Product;
import com.latienditadeportiva.Inventory_ms.models.Inventory;
import com.latienditadeportiva.Inventory_ms.repositories.ProductRepository;
import com.latienditadeportiva.Inventory_ms.repositories.InventoryRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class InventoryController {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public InventoryController(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @PostMapping("/inventorysale")
    Inventory InventorySale(@RequestBody Inventory inventory){
        Product product = productRepository.findById(inventory.getProductId()).orElse(null);

        if (product == null)
            throw new ProductNotFoundException("No se encontró un producto con el ID: " + inventory.getProductId());

        if(product.getStock() < inventory.getStock())
            throw new NoStockException("No hay unidades en el inventario");

        if (inventory.getStock() <= 0)
            throw new NoStockException("La operacion no es valida");

        //Se descuentan los productos del stock
        product.setStock( product.getStock() - inventory.getStock() );
        //Se suma la cantidad de productos comprados al contador de ventas del producto
        product.setSales( product.getSales() + inventory.getStock() );
        productRepository.save(product);

        inventory.setMovement("Salida");
        inventory.setModifyDate(new Date());
        return inventoryRepository.save(inventory);
    }

    @PostMapping("/inventoryadd")
    Inventory InventoryAdd(@RequestBody Inventory inventory){
        Product productId = productRepository.findById(inventory.getProductId()).orElse(null);

        if (productId == null)
            throw new ProductNotFoundException("No se encontró un producto con el ID: " + inventory.getProductId());

        if (inventory.getStock() <= 0)
            throw new NoStockException("La operacion no es valida");


        productId.setStock(productId.getStock() + inventory.getStock());
        productRepository.save(productId);

        inventory.setMovement("Ingreso");
        inventory.setModifyDate(new Date());
        return inventoryRepository.save(inventory);
    }

    @GetMapping("/inventory/{productId}")
    List<Inventory> inventoryMovement(@PathVariable String productId){
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null)
            throw new ProductNotFoundException("No se encontró un producto con el ID: " + productId);

        List<Inventory> stockHistory = inventoryRepository.findByProductId(productId);

        return stockHistory;
    }
}
