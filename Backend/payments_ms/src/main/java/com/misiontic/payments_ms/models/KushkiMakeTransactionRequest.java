package com.misiontic.payments_ms.models;

public class KushkiMakeTransactionRequest {
    private String token;
    private Amount amount;

    public KushkiMakeTransactionRequest(String token, Amount amount) {
        this.token = token;
        this.amount = amount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }
}
