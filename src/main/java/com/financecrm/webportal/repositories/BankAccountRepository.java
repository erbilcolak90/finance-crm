package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.BankAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BankAccountRepository extends MongoRepository<BankAccount,String> {
}
