package com.latienditadeportiva.Inventory_ms.dto.order;

public class OrderIdUsernameDto {
    private String username;
    private String orderId;

    public OrderIdUsernameDto(String username, String orderId) {
        this.username = username;
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
