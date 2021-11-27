package com.misiontic.payments_ms.models;

public class KushkiURLGenerated {
    private String bankId;
    private String bankName;
    private String redirectUrl;
    private String transactionReference;
    private String trazabilityCode;

    public KushkiURLGenerated(String bankId, String bankName, String redirectUrl, String transactionReference, String trazabilityCode) {
        this.bankId = bankId;
        this.bankName = bankName;
        this.redirectUrl = redirectUrl;
        this.transactionReference = transactionReference;
        this.trazabilityCode = trazabilityCode;
    }

    public KushkiURLGenerated(String redirectUrl, String transactionReference) {
        this.redirectUrl = redirectUrl;
        this.transactionReference = transactionReference;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getTrazabilityCode() {
        return trazabilityCode;
    }

    public void setTrazabilityCode(String trazabilityCode) {
        this.trazabilityCode = trazabilityCode;
    }
}
