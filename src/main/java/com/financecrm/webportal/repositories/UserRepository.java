package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {

    User findByName(String username);

    User findByEmail(String email);

    @Query(value = "{ 'isDeleted': false }")
    Page<User> findByIsDeletedFalse(Pageable pageable);

    @Query(value = "{'id': ?0 , 'isDeleted': false}")
    User findByIdAndIsDeletedFalse(String userId);

}
