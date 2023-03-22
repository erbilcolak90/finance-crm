package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.enums.Role;
import com.financecrm.webportal.enums.UserStatus;
import com.financecrm.webportal.input.role.AddRoleToUserInput;
import com.financecrm.webportal.input.user.UserInput;
import com.financecrm.webportal.payload.user.UserPayload;
import com.financecrm.webportal.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private WalletAccountService walletAccountService;

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
        User db_user = userRepository.findByEmail(userInput.getEmail());
        if(db_user == null){
            User user = new User();
            Date date = new Date();
            user.setEmail(userInput.getEmail());
            user.setPassword(bCryptPasswordEncoder.encode(userInput.getPassword()));
            user.setName(userInput.getName());
            user.setSurname(userInput.getSurname());
            user.setPhone(userInput.getPhone());
            user.setStatus(UserStatus.WAITING);
            user.setRepresentativeEmployeeId(null);
            user.setDeleted(false);
            user.setCreateDate(date);
            user.setUpdateDate(date);
            userRepository.save(user);
            log.info(user.getId()+ " is signed");
            AddRoleToUserInput addRoleToUserInput = new AddRoleToUserInput(user.getId(), Role.USER.toString());
            userRoleService.addRoleToUser(addRoleToUserInput);
            log.info(user.getId()+ " ROLE.USER added ");
            walletAccountService.createWalletAccount(user.getId());
            log.info(user.getId()+ " 's wallet created");

            return true;
        }
        else{
            return false;
        }
    }

    public UserPayload getUserById(String id){
        User db_user = userRepository.findById(id).orElse(null);
        List<String> db_userRoles = userRoleService.getUserRolesByUserId(id);
        if(db_user != null){
            UserPayload user = new UserPayload();
            user.setId(db_user.getId());
            user.setName(db_user.getName());
            user.setSurname(db_user.getSurname());
            user.setEmail(db_user.getEmail());
            user.setPhone(db_user.getPhone());
            user.setStatus(db_user.getStatus());
            user.setRoles(db_userRoles);
            return user;
        }
        return null;
    }

}
