package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.TradingAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TradingAccountRepository extends MongoRepository<TradingAccount,String> {

    @Query(value = "{'userId': ?0 }")
    Page<TradingAccount> getAllTradingAccountsByUserId(String userId,Pageable pageable);
}
