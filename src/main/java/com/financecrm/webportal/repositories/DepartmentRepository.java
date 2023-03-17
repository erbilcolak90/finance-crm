package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DepartmentRepository extends MongoRepository<Department,String> {
}
