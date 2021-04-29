package com.project_vector_backend.controllers;

import java.util.List;
import java.util.Optional;

import com.project_vector_backend.models.AuthUser;
import com.project_vector_backend.models.Document;
import com.project_vector_backend.repositories.DocumentRepository;
import com.project_vector_backend.repositories.AuthUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/document")
public class DocumentController {

  @Autowired
  private DocumentRepository documentRepository;

  @Autowired
  private AuthUserRepository authUserRepository;

  @GetMapping
  public ResponseEntity<List<Document>> read(@AuthenticationPrincipal AuthUser authUser) {
    AuthUser user = authUserRepository.getById(authUser.getId());
    List<Document> documents = user.getDocuments();

    return ResponseEntity.ok().body(documents);
  }

  @PostMapping
  public ResponseEntity<Object> create(@AuthenticationPrincipal AuthUser authUser,
      @Valid @RequestBody Document document) {

    if (documentRepository.existsByIdentificationCode(document.getIdentificationCode())) {
      return ResponseEntity.badRequest().body("This document is already registered.");
    }

    AuthUser user = authUserRepository.getById(authUser.getId());
    List<Document> documents = user.getDocuments();

    documents.add(document);
    user.setDocuments(documents);
    authUserRepository.save(user);

    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> update(@PathVariable("id") Long id, @AuthenticationPrincipal AuthUser authUser,
      @RequestBody @Valid Document document) {

    AuthUser user = authUserRepository.getById(authUser.getId());
    Optional<Document> foundDocument = documentRepository.findById(id);
    if (foundDocument.isPresent()) {

      Document updatedDocument = foundDocument.get();

      if (!document.getIdentificationCode().equals(updatedDocument.getIdentificationCode())
          && documentRepository.existsByIdentificationCode(document.getIdentificationCode())) {
        return ResponseEntity.badRequest().body("This document is already registered.");
      }

      updatedDocument = document;
      updatedDocument.setId(id);
      updatedDocument.setAuthUser(user);
      documentRepository.save(updatedDocument);

      return ResponseEntity.ok(updatedDocument);
    }
    return ResponseEntity.notFound().build();

  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
    Optional<Document> foundDocument = documentRepository.findById(id);
    if (foundDocument.isPresent()) {
      documentRepository.delete(foundDocument.get());
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();

  }
}
