package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface DepartmentRepository extends MongoRepository<Department, String> {

    @Query(value = "{'name': ?0 , 'isDeleted': false }")
    Optional<Department> findByName(String departmentName);

    @Query(value = "{'id': ?0 , 'isDeleted': false }")
    Optional<Department> findById(String id);

    @Query(value = "{'isDeleted': false }")
    Page<Department> findByIsDeletedFalse(Pageable pageable);
}
