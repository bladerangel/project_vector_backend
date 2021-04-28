package com.example.vector.projeto_vector.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.vector.projeto_vector.models.AuthUser;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
  Optional<AuthUser> findByEmail(String email);

  Boolean existsByEmail(String email);
}
