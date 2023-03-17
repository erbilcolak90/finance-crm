package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.Transfer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransferRepository extends MongoRepository<Transfer,String> {
}
