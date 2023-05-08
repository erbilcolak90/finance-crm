package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface BankAccountRepository extends MongoRepository<BankAccount, String> {

    @Query(value = "{'iban': ?0 , 'isDeleted': false}")
    Optional<BankAccount> findByIban(String iban);

    @Query(value = "{'userId': ?0 , 'isDeleted': false }")
    Page<BankAccount> findByUserIdAndIsDeletedFalse(String userId, Pageable pageable);

    @Query(value = "{'id': ?0 , 'isDeleted': false}")
    Optional<BankAccount> findById(String id);
}
