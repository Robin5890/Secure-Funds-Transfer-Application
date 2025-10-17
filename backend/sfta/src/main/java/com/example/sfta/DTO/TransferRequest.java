package com.example.sfta.DTO;

import java.math.BigDecimal;

public class TransferRequest {

    private String recipientID;
    private BigDecimal amount;

    
    public TransferRequest(String recipientID, BigDecimal amount) {
        this.recipientID = recipientID;
        this.amount = amount;
    }

    public String getRecipientID() {
        return recipientID;
    }
    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    


}
