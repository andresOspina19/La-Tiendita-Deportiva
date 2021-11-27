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


class Amount {
    public double subtotalIva;
    public double subtotalIva0;
    public double iva;

    public Amount(double subtotalIva, double subtotalIva0, double iva) {
        this.subtotalIva = subtotalIva;
        this.subtotalIva0 = subtotalIva0;
        this.iva = iva;
    }

    public double getSubtotalIva() {
        return subtotalIva;
    }

    public void setSubtotalIva(double subtotalIva) {
        this.subtotalIva = subtotalIva;
    }

    public double getSubtotalIva0() {
        return subtotalIva0;
    }

    public void setSubtotalIva0(double subtotalIva0) {
        this.subtotalIva0 = subtotalIva0;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }
}
