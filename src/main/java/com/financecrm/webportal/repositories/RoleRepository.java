package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    @Query(value = "{'name': ?0 , 'isDeleted': false }")
    Optional<Role> findByName(String roleName);

    @Query(value = "{'isDeleted': false }")
    Page<Role> findByIsDeletedFalse(Pageable pageable);
}
