package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.TradingAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TradingAccountRepository extends MongoRepository<TradingAccount,String> {
}
