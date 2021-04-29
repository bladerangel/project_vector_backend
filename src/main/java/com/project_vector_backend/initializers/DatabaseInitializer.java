package com.project_vector_backend.initializers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import com.project_vector_backend.models.AuthUser;
import com.project_vector_backend.models.Document;
import com.project_vector_backend.repositories.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

  @Autowired
  private AuthUserRepository userRepository;

  @PostConstruct
  public void init() {
    AuthUser admin = new AuthUser();
    admin.setUsername("admin");
    admin.setPassword("admin");

    List<Document> documents = new ArrayList<Document>();

    for (int i = 0; i < 10; i++) {
      Document document = new Document();
      document.setIdentificationCode("00" + i + "/201" + i);
      document.setDescription("Oficio" + " 00" + i + "/201" + i
          + " oriundo da Prefeitura Municipal de Fortaleza, acerca de demandas ambientais.");
      document.setPersonRequested("Pedro Rangel");
      documents.add(document);
    }

    admin.setDocuments(documents);
    userRepository.save(admin);
  }
}
