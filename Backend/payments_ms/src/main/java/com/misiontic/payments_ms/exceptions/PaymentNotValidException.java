package com.misiontic.payments_ms.exceptions;

public class PaymentNotValidException extends RuntimeException {

    public PaymentNotValidException(String message) {
        super(message);
    }
}