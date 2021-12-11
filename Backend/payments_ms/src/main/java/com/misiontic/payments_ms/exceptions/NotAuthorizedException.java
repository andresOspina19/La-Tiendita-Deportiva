package com.misiontic.payments_ms.exceptions;

public class NotAuthorizedException  extends RuntimeException {
    public NotAuthorizedException(String message) {
        super(message);
    }
}
