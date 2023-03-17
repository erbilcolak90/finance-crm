package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRoleRepository extends MongoRepository<UserRole,String> {

    List<UserRole> findByUserIdAndRoleId (String userId, String roleId);

/*    @Query(value = "{ 'id' : ?0}", fields = "{ 'roleId'}")
    List<String> getByUserRoles(String userId);*/

    List<String> getUserRolesByUserId(String userId);
}
