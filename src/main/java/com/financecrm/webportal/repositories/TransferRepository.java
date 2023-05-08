package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface TransferRepository extends MongoRepository<Transfer, String> {

    @Query(value = "{'userId': ?0 , 'isDeleted': false }")
    Page<Transfer> findByUserIdAndIsDeletedFalse(String userId, Pageable pageable);

    @Query(value = "{'id': ?0 , 'isDeleted': false}")
    Optional<Transfer> findById(String id);
}
