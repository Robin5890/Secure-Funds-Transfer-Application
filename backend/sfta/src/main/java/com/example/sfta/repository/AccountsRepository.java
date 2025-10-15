package com.example.sfta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sfta.model.Account;

public interface AccountsRepository extends JpaRepository<Account,Integer>{

}
