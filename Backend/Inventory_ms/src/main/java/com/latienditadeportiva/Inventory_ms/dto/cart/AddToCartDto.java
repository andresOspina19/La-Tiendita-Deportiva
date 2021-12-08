package com.latienditadeportiva.Inventory_ms.dto.cart;

import javax.validation.constraints.NotNull;

public class AddToCartDto {
    private String id;
    @NotNull
    private String productId;
    @NotNull
    private Integer quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
