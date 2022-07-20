package com.misiontic.payments_ms.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

public class KushkiPayment {
    @Id
    private String token;
    private String username;
    private Amount amount;
    private String status;
    private String bankurl;
    private Date date;

    public KushkiPayment(String token, String username, Amount amount, String status, String bankurl, Date date) {
        this.token = token;
        this.username = username;
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
