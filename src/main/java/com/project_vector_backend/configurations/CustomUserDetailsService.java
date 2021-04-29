package com.project_vector_backend.configurations;

import javax.transaction.Transactional;

import com.project_vector_backend.models.AuthUser;
import com.project_vector_backend.repositories.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  AuthUserRepository authUserRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    AuthUser authUser = authUserRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));

    return authUser;
  }

  @Transactional
  public UserDetails loadUserById(Long id) {
    AuthUser authUser = authUserRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));

    return authUser;
  }
}
