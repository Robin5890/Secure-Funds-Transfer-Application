package com.example.sfta.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionHistory {

  private String senderAccount;
  private String receiverAccount;
  private BigDecimal amount;
  private LocalDateTime timestamp;

  public TransactionHistory(String senderAccount, String receiverAccount, BigDecimal amount, LocalDateTime timestamp) {
    this.senderAccount = senderAccount;
    this.receiverAccount = receiverAccount;
    this.amount = amount;
    this.timestamp = timestamp;
  }

  public String getSenderAccount() {
    return senderAccount;
  }

  public void setSenderAccount(String senderAccount) {
    this.senderAccount = senderAccount;
  }

  public String getReceiverAccount() {
    return receiverAccount;
  }

  public void setReceiverAccount(String receiverAccount) {
    this.receiverAccount = receiverAccount;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

}
