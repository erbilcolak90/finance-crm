package com.financecrm.webportal.repositories;

import com.financecrm.webportal.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {

    User findByName(String username);

    User findByEmail(String email);

}
