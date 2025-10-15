package com.example.sfta;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class tre {
public static void main(String[] args) {


    BCryptPasswordEncoder bc = new BCryptPasswordEncoder();

    System.out.println(bc.encode("password123"));
}
}
