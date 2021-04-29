package com.project_vector_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project_vector_backend.models.Document;

import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
  Boolean existsByIdentificationCode(String identificationCode);
}
