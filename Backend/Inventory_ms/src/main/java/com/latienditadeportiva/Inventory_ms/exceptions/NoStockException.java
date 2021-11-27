package com.latienditadeportiva.Inventory_ms.exceptions;

public class NoStockException extends RuntimeException {

    public NoStockException(String message) {
        super(message);
    }
}