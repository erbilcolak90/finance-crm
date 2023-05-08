package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    @Query(value = "{'username': ?0 , 'isDeleted': false }")
    Optional<User> findByName(String username);

    @Query(value = "{'email': ?0 , 'isDeleted': false }")
    Optional<User> findByEmail(String email);

    @Query(value = "{ 'isDeleted': false }")
    Page<User> findByIsDeletedFalse(Pageable pageable);

    @Query(value = "{'id': ?0 , 'isDeleted': false}")
    Optional<User> findById(String userId);

}
