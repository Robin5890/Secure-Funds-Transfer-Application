package com.example.sfta.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.sfta.Config.JwUtil;
import com.example.sfta.DTO.UserData;
import com.example.sfta.model.Account;
import com.example.sfta.model.User;
import com.example.sfta.repository.AccountsRepository;
import com.example.sfta.repository.UserRepository;

@Service
public class LoginService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  private AccountsRepository accountsRepository;

  @Autowired
  private JwUtil JwUtil;

  public String login(String username, String password) {
    User user = userRepository.findByUsername(username).orElse(null);

    if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
      throw new RuntimeException("Invalid username or password");
    }

    return JwUtil.generateToken(user.getId());

  }

  public UserData getUserData(User user) {

    Account account = accountsRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Account not found"));

    return new UserData(
        user.getUsername(),
        account.getBalance(),
        account.getAccountnumber());
  }

}
