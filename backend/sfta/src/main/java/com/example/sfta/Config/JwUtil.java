package com.example.sfta.Config;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwUtil {

  private static final String SECRET = "cB2pCDD19BfjgiR9Tsw40j6zyNz0uhxZ";
  private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
  public static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

  public String generateToken(Integer id) {
    return Jwts.builder()
        .setSubject(id.toString())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(SECRET_KEY)
        .compact();
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(SECRET_KEY)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public Integer extractUserId(String token) {
    String subject = extractAllClaims(token).getSubject();
    return Integer.parseInt(subject);
  }

  public boolean isTokenValid(String token) {
    return !extractAllClaims(token).getExpiration().before(new Date());
  }

}
