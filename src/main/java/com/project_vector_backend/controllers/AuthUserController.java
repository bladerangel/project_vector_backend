package com.project_vector_backend.controllers;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.project_vector_backend.configurations.JwtTokenProvider;

import com.project_vector_backend.models.AuthUser;

import com.project_vector_backend.repositories.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/user")
public class AuthUserController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  AuthUserRepository authUserRepository;

  @Autowired
  JwtTokenProvider tokenProvider;

  @PostMapping("/signin")
  public ResponseEntity<Object> authenticateUser(@Valid @RequestBody AuthUser authUser) throws ParseException {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(authUser.getEmail(), authUser.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = tokenProvider.generateToken(authentication);

    Date expiryDate = tokenProvider.getExpiryDateFromJWT(jwt);

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    Optional<AuthUser> authUserFound = authUserRepository.findByEmail(authUser.getEmail());
    Map<String, Object> response = new HashMap<>();
    response.put("user", authUserFound);
    response.put("token", jwt);
    response.put("expiryDate", formatter.format(expiryDate));

    return ResponseEntity.ok(response);
  }

  @PostMapping("/signup")
  public ResponseEntity<Object> registerUser(@Valid @RequestBody AuthUser authUser) {
    authUser.setId(null);

    if (authUserRepository.existsByEmail(authUser.getEmail())) {
      return ResponseEntity.badRequest().body("This email is already registered.");
    }

    URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}")
        .buildAndExpand(authUser.getUsername()).toUri();

    try {
      return ResponseEntity.created(location).body(authUserRepository.save(authUser));
    } catch (Exception error) {
      return ResponseEntity.notFound().build();
    }
  }

}