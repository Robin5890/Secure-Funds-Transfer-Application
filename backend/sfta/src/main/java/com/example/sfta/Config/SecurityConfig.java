package com.example.sfta.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())  // disable CSRF for testing
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/users", "/accounts").permitAll() // public endpoints
            .anyRequest().authenticated() // everything else requires auth
        )
        .formLogin(form -> form.disable()); // disable default login page

    return http.build();
}


}
