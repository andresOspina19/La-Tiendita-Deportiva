package com.misiontic.payments_ms.models;

public class Amount {
    private double subtotalIva;
    private double subtotalIva0;
    private double iva;

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
