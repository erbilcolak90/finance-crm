package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.enums.Role;
import com.financecrm.webportal.enums.UserStatus;
import com.financecrm.webportal.input.UserInput;
import com.financecrm.webportal.payload.UserPayload;
import com.financecrm.webportal.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CustomUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findByName(String username) {
        return userRepository.findByName(username);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User findByUserId(String userId){
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public boolean signUp(UserInput userInput){
        User user = userRepository.findByEmail(userInput.getEmail());
        if(user == null){
            User db_user = new User();
            //OffsetDateTime date = OffsetDateTime.now();
            Date date = new Date();
            db_user.setEmail(userInput.getEmail());
            db_user.setPassword(bCryptPasswordEncoder.encode(userInput.getPassword()));
            db_user.setName(userInput.getName());
            db_user.setSurname(userInput.getSurname());
            db_user.setPhone(userInput.getPhone());
            db_user.setStatus(UserStatus.WAITING);
            db_user.setRepresentativeEmployeeId(null);
            db_user.setDeleted(false);
            db_user.setCreateDate(date);
            db_user.setUpdateDate(date);
            userRepository.save(db_user);
            userRoleService.addRoleToUser(db_user.getId(), Role.USER.toString());

            return true;
        }
        else{
            return false;
        }
    }

    public UserPayload getUserById(String id){
        User db_user = userRepository.findById(id).orElse(null);
        if(db_user != null){
            UserPayload data = new UserPayload();
            data.setId(db_user.getId());
            data.setName(db_user.getName());
            data.setSurname(db_user.getSurname());
            data.setEmail(db_user.getEmail());
            data.setPhone(db_user.getPhone());
            data.setStatus(db_user.getStatus());
            return data;
        }
        return null;
    }

}
