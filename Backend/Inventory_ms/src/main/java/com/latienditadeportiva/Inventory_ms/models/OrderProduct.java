package com.latienditadeportiva.Inventory_ms.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

public class OrderProduct {
    @Id
    private String orderProductId;
    private String orderId;
    private Product product;
    private double price;
    private Integer quantity;
    private Date createdDate;

    public OrderProduct(){}

    public OrderProduct(String orderProductId, String orderId, Product product, double price, Integer quantity) {
        this.orderProductId = orderProductId;
        this.orderId = orderId;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.createdDate = new Date();
    }

    public String getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(String orderProductId) {
        this.orderProductId = orderProductId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
