package com.example.sfta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sfta.model.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

  List<Transactions> findBySenderAccountOrReceiverAccount(String sender, String receiver);

}
