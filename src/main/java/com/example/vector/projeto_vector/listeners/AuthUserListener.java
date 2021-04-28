package com.example.vector.projeto_vector.listeners;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.example.vector.projeto_vector.models.AuthUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthUserListener {

  @Autowired
  PasswordEncoder passwordEncoder;

  @PrePersist
  @PreUpdate
  public void encodePassword(AuthUser authUser) {
    authUser.setPassword(passwordEncoder.encode(authUser.getPassword()));
  }

}