package com.latienditadeportiva.Inventory_ms.models;

import org.springframework.data.annotation.Id;
import java.util.Date;

public class Inventory {
    @Id
    private String id;
    private String productId;
    private Integer stock;
    private String movement;
    private Date modifyDate;

    public Inventory(String id, String productId, Integer stock, String movement, Date modifyDate) {
        this.id = id;
        this.productId = productId;
        this.stock = stock;
        this.movement = movement;
        this.modifyDate = modifyDate;
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getMovement() {
        return movement;
    }

    public void setMovement(String movement) {
        this.movement = movement;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}