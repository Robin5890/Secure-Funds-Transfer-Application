package com.example.sfta.Config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import com.example.sfta.model.User;
import com.example.sfta.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwUtil jwUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    Cookie cookie = WebUtils.getCookie(request, "jwt");
    if (cookie != null) {
      String token = cookie.getValue();
      if (token != null) {
        if (jwUtil.isTokenValid(token)) {
          Integer id = jwUtil.extractUserId(token);
          User user = userRepository.findById(id).orElse(null);
          if (user != null) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);
          }
        }
      }
    }

    filterChain.doFilter(request, response);
  }

}
