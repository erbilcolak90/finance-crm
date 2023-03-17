package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.UserValidationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserValidationDocumentRepository extends MongoRepository<UserValidationDocument,String> {
}
