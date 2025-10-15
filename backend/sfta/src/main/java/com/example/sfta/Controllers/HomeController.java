package com.example.sfta.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.sfta.Config.JwUtil;
import com.example.sfta.DTO.LoginRequest;
import com.example.sfta.model.User;
import com.example.sfta.repository.AccountsRepository;
import com.example.sfta.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
public class HomeController {

@Autowired
private UserRepository userRepository;

@Autowired
private AccountsRepository accountsRepository;

@Autowired
private BCryptPasswordEncoder passwordEncoder;

@Autowired
private JwUtil jwUtil;

@GetMapping("/users")
public String getUsers() {
    return userRepository.findAll().toString();
}

@GetMapping("/accounts")
public String getAccounts() {
    return accountsRepository.findAll().toString();
}


@PostMapping("/login")
public ResponseEntity<String> auth(@RequestBody LoginRequest request) {
   
    User user = userRepository.findByUsername(request.getUsername()).orElse(null);

    if(user == null){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
    }
   
    if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(passwordEncoder.encode(request.getPassword()) +"++"+ user.getPassword());
    }
  
    String token = jwUtil.generateToken(user.getUsername());


   return ResponseEntity.ok(token);
     
}




}
