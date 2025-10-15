package com.example.sfta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sfta.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{
}
