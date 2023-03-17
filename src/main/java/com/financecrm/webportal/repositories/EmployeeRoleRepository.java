package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.EmployeeRole;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRoleRepository extends MongoRepository<EmployeeRole,String> {
}
