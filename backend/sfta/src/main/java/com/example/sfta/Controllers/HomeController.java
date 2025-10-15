package com.example.sfta.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.sfta.repository.AccountsRepository;
import com.example.sfta.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class HomeController {

@Autowired
private UserRepository userRepository;

@Autowired
private AccountsRepository accountsRepository;

@GetMapping("/Users")
public String getUsers() {
    return userRepository.findAll().toString();
}

@GetMapping("/Accounts")
public String getAccounts() {
    return accountsRepository.findAll().toString();
}



}
