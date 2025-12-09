package com.example.sfta.Services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sfta.DTO.TransactionHistory;
import com.example.sfta.model.Account;
import com.example.sfta.model.Transactions;
import com.example.sfta.model.User;
import com.example.sfta.repository.AccountsRepository;
import com.example.sfta.repository.TransactionsRepository;

import jakarta.transaction.Transactional;

@Service
public class TransferService {

  @Autowired
  private AccountsRepository accountsRepository;

  @Autowired
  private TransactionsRepository transactionsRepository;

  @Transactional
  public void transferFunds(User sender, String receiverAccountNumber, BigDecimal amount) {

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Transfer amount must be positive");
    }

    Account senderAccount = accountsRepository.findByUser(sender)
        .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));

    Account receiverAccount = accountsRepository.findByAccountNumber(receiverAccountNumber)
        .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

    if (senderAccount.getAccountnumber().equals(receiverAccountNumber)) {
      throw new IllegalArgumentException("Sender and receiver cannot be the same account");
    }

    if (senderAccount.getBalance().compareTo(amount) < 0) {
      throw new IllegalArgumentException("Insufficient funds");
    }

    senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
    receiverAccount.setBalance(receiverAccount.getBalance().add(amount));

    Transactions tr = new Transactions(senderAccount.getAccountnumber(), receiverAccount.getAccountnumber(), amount);

    transactionsRepository.save(tr);

    accountsRepository.save(senderAccount);
    accountsRepository.save(receiverAccount);

  }

  public List<TransactionHistory> getTransferHistory(User user) {
    Account account = accountsRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Account not found"));
    return transactionsRepository
        .findBySenderAccountOrReceiverAccount(account.getAccountnumber(), account.getAccountnumber()).stream()
        .map(t -> new TransactionHistory(t.getSenderAccount(), t.getReceiverAccount(), t.getAmount(), t.getTimestamp()))
        .toList();
  }

}
