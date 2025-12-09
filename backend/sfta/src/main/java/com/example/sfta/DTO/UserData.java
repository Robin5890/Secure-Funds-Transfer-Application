package com.example.sfta.DTO;

import java.math.BigDecimal;

public class UserData {
  private String username;
  private BigDecimal balance;
  private String accountNumber;

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public UserData(String username, BigDecimal balance, String accountNumber) {
    this.username = username;
    this.balance = balance;
    this.accountNumber = accountNumber;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }
}
