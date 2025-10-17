package com.example.sfta.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.sfta.Config.JwUtil;
import com.example.sfta.DTO.LoginRequest;
import com.example.sfta.DTO.TransferRequest;
import com.example.sfta.DTO.UserData;
import com.example.sfta.model.Account;
import com.example.sfta.model.User;
import com.example.sfta.repository.AccountsRepository;
import com.example.sfta.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;







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




@GetMapping("/getUserData")
public ResponseEntity<UserData> userData(HttpServletRequest request) {

        
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = header.substring(7);

        if (!jwUtil.isTokenValid(token)) {
            return ResponseEntity.status(401).build();
        }


        String username = jwUtil.extractUsername(token);

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).build();
        }

        Account account = accountsRepository.findByUser(user).orElse(null);
        if (account == null) {
            return ResponseEntity.status(404).build();
        }

        UserData dto = new UserData(user.getUsername(), account.getBalance());

        return ResponseEntity.ok(dto);
}


@PostMapping("/transfer")
@Transactional
public ResponseEntity<String> transferBalance(@RequestBody TransferRequest request, HttpServletRequest serverRequest) {
   
    String header = serverRequest.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = header.substring(7);

        if (!jwUtil.isTokenValid(token)) {
            return ResponseEntity.status(401).build();
        }


        String username = jwUtil.extractUsername(token);

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).build();
        }

        Account senderAccount = accountsRepository.findByUser(user).orElse(null);
        if (senderAccount == null) {
            return ResponseEntity.status(404).build();
        }

        Account receiverAccount = accountsRepository.findByAccountNumber(request.getRecipientID()).orElse(null);
        if (receiverAccount == null) {
            return ResponseEntity.status(404).build();
        }

        if (request.getAmount() == null || senderAccount.getBalance().compareTo(request.getAmount()) < 0) {
    return ResponseEntity.badRequest().body("Invalid or insufficient amount");
}


        senderAccount.setBalance(senderAccount.getBalance().subtract(request.getAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(request.getAmount()));

        accountsRepository.save(senderAccount);
        accountsRepository.save(receiverAccount);

        return ResponseEntity.ok("Transfer complete");
}



}
