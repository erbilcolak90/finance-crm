package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TeamRepository extends MongoRepository<Team,String> {

    @Query(value = "{'isDeleted': false }")
    Team findByName(String name);

    @Query(value = "{'isDeleted': false }")
    Page<Team> findByIsDeletedFalse(Pageable pageable);
}
