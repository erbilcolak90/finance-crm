package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface EmployeeRepository extends MongoRepository<Employee,String> {


    Employee findByEmail(String email);

    @Query(value = "{ 'isDeleted': false }")
    Page<Employee> findByIsDeletedFalse(Pageable pageable);
}
