package com.misiontic.payments_ms.models;

public class KushkiPaymentStatus {
    private String status;
    private String bankurl;

    public KushkiPaymentStatus(String status, String bankurl) {
        this.status = status;
        this.bankurl = bankurl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBankurl() {
        return bankurl;
    }

    public void setBankurl(String bankurl) {
        this.bankurl = bankurl;
    }
}
