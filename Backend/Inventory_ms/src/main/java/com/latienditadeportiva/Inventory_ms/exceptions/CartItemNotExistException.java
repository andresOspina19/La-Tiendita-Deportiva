package com.latienditadeportiva.Inventory_ms.exceptions;

public class CartItemNotExistException extends RuntimeException {
    public CartItemNotExistException(String message) {
        super(message);
    }
}
