package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.EmployeeRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EmployeeRoleRepository extends MongoRepository<EmployeeRole,String> {

    List<EmployeeRole> findAllByEmployeeId(String employeeId);

    @Query(value = "{'employeeId': ?0 , 'roleId' : ?1 , 'isDeleted': false}")
    EmployeeRole findByEmployeeIdAndRoleId(String employeeId, String roleId);
}
