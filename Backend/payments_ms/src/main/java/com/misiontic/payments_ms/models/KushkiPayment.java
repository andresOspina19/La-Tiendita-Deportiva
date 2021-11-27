package com.misiontic.payments_ms.models;

import org.springframework.data.annotation.Id;
import java.util.Date;

public class KushkiPayment {
    @Id
    private String token;
    private String username;
    private Long purchaseId;
    private Amount amount;
    private Date date;

    public KushkiPayment(String token, Long userId, Long purchaseId, Amount amount, Date date) {
        this.token = token;
        this.userId = userId;
        this.purchaseId = purchaseId;
        this.amount = amount;
        this.date = date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
