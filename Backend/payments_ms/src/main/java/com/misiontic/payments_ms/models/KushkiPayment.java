package com.misiontic.payments_ms.models;

import org.springframework.data.annotation.Id;
import java.util.Date;

public class KushkiPayment {
    @Id
    private String token;
    private String username;
    private String purchaseId;
    private Amount amount;
    private String status;
    private String bankurl;
    private Date date;

    //Para el pago, a espera del status para confirmar que el pago fue exitoso
    public KushkiPayment(String token, String username, String purchaseId, Amount amount, Date date) {
        this.token = token;
        this.username = username;
        this.purchaseId = purchaseId;
        this.amount = amount;
        this.date = date;
    }

    //Para el pago con todos los datos
    public KushkiPayment(String token, String username, String purchaseId, Amount amount, String status, String bankurl, Date date) {
        this.token = token;
        this.username = username;
        this.purchaseId = purchaseId;
        this.amount = amount;
        this.status = status;
        this.bankurl = bankurl;
        this.date = date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
