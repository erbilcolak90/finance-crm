package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRoleRepository extends MongoRepository<UserRole, String> {

    List<UserRole> findByUserIdAndRoleId(String userId, String roleId);

    List<UserRole> getUserRolesByUserId(String userId);
}
