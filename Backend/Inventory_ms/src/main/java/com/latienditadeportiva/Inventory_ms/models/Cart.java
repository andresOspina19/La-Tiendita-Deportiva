package com.latienditadeportiva.Inventory_ms.models;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class Cart {
    @Id
    private String id;
    private String username;
    private String productId;
    private int quantity;
    private Date createdDate;

    public Cart(String id, String username, String productId, int quantity, Date createdDate) {
        this.id = id;
        this.username = username;
        this.productId = productId;
        this.quantity = quantity;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
