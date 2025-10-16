package com.example.sfta.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sfta.model.Account;
import com.example.sfta.model.User;

public interface AccountsRepository extends JpaRepository<Account,Integer>{

    Optional<Account> findByUser(User user);
    

}
