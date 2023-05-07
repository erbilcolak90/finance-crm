package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByName(String roleName);
}
