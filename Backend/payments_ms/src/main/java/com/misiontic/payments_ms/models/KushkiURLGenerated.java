package com.misiontic.payments_ms.models;

public class KushkiURLGenerated {
    private String redirectUrl;
    private String transactionReference;

    public KushkiURLGenerated(String redirectUrl, String transactionReference) {
        this.redirectUrl = redirectUrl;
        this.transactionReference = transactionReference;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }
}
