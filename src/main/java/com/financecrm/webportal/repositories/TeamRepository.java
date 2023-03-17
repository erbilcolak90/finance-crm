package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team,String> {
}
