package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.Process;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProcessRepository extends MongoRepository<Process,String> {
}
