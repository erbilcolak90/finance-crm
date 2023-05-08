package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRoleRepository extends MongoRepository<UserRole, String> {

    @Query(value = "{'userId': ?0 , 'roleId': ?1 , 'isDeleted':false}")
    List<UserRole> findByUserIdAndRoleId(String userId, String roleId);

    @Query(value = "{'userId': ?0 , 'isDeleted': false}")
    List<UserRole> getUserRolesByUserId(String userId);
}
