package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.WalletAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WalletAccountRepository extends MongoRepository<WalletAccount, String> {

    WalletAccount getByUserId(String userId);

}
