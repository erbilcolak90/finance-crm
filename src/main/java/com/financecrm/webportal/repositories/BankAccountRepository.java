package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface BankAccountRepository extends MongoRepository<BankAccount, String> {

    @Query(value = "{'iban': ?0 , 'isDeleted': false}")
    BankAccount findByIban(String iban);

    @Query(value = "{'userId': ?0 , 'isDeleted': false }")
    Page<BankAccount> findByUserIdAndIsDeletedFalse(String userId, Pageable pageable);
}
