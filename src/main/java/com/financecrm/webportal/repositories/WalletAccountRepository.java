package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.WalletAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface WalletAccountRepository extends MongoRepository<WalletAccount, String> {

    @Query(value = "{'userId': ?0 , 'isDeleted': false}")
    Optional<WalletAccount> findByUserId(String userId);

    @Query(value = "{'id': ?0 , 'isDeleted':false}")
    Optional<WalletAccount> findById(String id);

}
