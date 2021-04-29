package com.project_vector_backend.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.project_vector_backend.models.Document;

import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends PagingAndSortingRepository<Document, Long> {
  Boolean existsByIdentificationCode(String identificationCode);
}
