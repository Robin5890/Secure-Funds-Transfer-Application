package com.example.sfta.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.sfta.DTO.LoginRequest;
import com.example.sfta.DTO.TransactionHistory;
import com.example.sfta.DTO.TransferRequest;
import com.example.sfta.DTO.UserData;
import com.example.sfta.Services.LoginService;
import com.example.sfta.Services.TransferService;
import com.example.sfta.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class HomeController {

  @Autowired
  private LoginService loginService;

  @Autowired
  private TransferService transferService;

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> auth(@RequestBody LoginRequest request, HttpServletResponse response) {

    try {

      String token = loginService.login(request.getUsername(), request.getPassword());

      ResponseCookie cookie = ResponseCookie.from("jwt", token)
          .httpOnly(true)
          .secure(true)
          .path("/")
          .sameSite("None")
          .maxAge(24 * 60 * 60)
          .build();

      response.addHeader("Set-Cookie", cookie.toString());
      return ResponseEntity.ok(Map.of("message", "Login successful"));

    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("message", e.getMessage()));
    }
  }

  @GetMapping("/getUserData")
  public ResponseEntity<?> userData(HttpServletRequest request) {

    User user = (User) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();

    try {
      UserData data = loginService.getUserData(user);
      return ResponseEntity.ok(data);

    } catch (RuntimeException e) {
      return ResponseEntity.status(404)
          .body(Map.of("message", e.getMessage()));
    }
  }

  @PostMapping("/transfer")
  public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {

    User user = (User) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();

    try {
      transferService.transferFunds(
          user,
          request.getRecipientID(),
          request.getAmount());

      return ResponseEntity.ok(Map.of("message", "Transfer complete"));

    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(Map.of("message", e.getMessage()));
    }
  }

  @GetMapping("/api/getTransferHistory")
  public ResponseEntity<?> getTransferHistory() {

    User user = (User) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();

    try {
      List<TransactionHistory> history = transferService.getTransferHistory(user);
      return ResponseEntity.ok(history);

    } catch (RuntimeException e) {
      return ResponseEntity.status(404)
          .body(Map.of("message", e.getMessage()));
    }
  }

  @PostMapping("/api/logout")
  public ResponseEntity<Void> logout(HttpServletResponse response) {

    ResponseCookie cookie = ResponseCookie.from("jwt", "")
        .httpOnly(true)
        .secure(true)
        .path("/")
        .sameSite("None")
        .maxAge(0)
        .build();
    response.addHeader("Set-Cookie", cookie.toString());
    return ResponseEntity.ok().build();

  }

}
