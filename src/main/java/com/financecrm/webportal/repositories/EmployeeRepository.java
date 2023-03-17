package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRepository extends MongoRepository<Employee,String> {
}
