package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.UserValidationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserValidationDocumentRepository extends MongoRepository<UserValidationDocument, String> {

    @Query(value = "{'userId': ?0 , 'isDeleted':false}")
    List<UserValidationDocument> findAllByUserId(String userId);

    @Query(value = "{'id': ?0 , 'isDeleted':false}")
    Optional<UserValidationDocument> findById(String id);
}
