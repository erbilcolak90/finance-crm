package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.UserValidationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserValidationDocumentRepository extends MongoRepository<UserValidationDocument, String> {

    List<UserValidationDocument> findAllByUserId(String userId);
}
