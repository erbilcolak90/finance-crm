package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface DepartmentRepository extends MongoRepository<Department,String> {

    Department findByName(String departmentName);

    @Query(value = "{'isDeleted': false }")
    Page<Department> findByIsDeletedFalse(Pageable pageable);
}
