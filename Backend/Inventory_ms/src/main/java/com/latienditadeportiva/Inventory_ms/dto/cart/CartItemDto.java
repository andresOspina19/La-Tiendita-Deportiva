package com.latienditadeportiva.Inventory_ms.dto.cart;

import com.latienditadeportiva.Inventory_ms.models.Product;

import javax.validation.constraints.NotNull;

public class CartItemDto {
    private String id;
    @NotNull
    private Integer quantity;
    @NotNull
    private Product product;

    public CartItemDto() {
    }

    public CartItemDto(String id, Integer quantity, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
