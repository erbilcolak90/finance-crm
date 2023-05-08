package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.enums.Role;
import com.financecrm.webportal.enums.UserStatus;
import com.financecrm.webportal.input.userrole.AddRoleToUserInput;
import com.financecrm.webportal.input.user.GetUserByIdInput;
import com.financecrm.webportal.input.user.SignUpInput;
import com.financecrm.webportal.payload.user.UserPayload;
import com.financecrm.webportal.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private WalletAccountService walletAccountService;
    @Autowired
    private MapperService mapperService;


    public User findByName(String username) {
        return userRepository.findByName(username).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User findByUserId(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public boolean signUp(SignUpInput signUpInput) {
        User userAtDatabase = userRepository.findByEmail(signUpInput.getEmail()).orElse(null);
        if (userAtDatabase == null) {
            User user = new User();
            Date date = new Date();
            user.setEmail(signUpInput.getEmail());
            user.setPassword(bCryptPasswordEncoder.encode(signUpInput.getPassword()));
            user.setName(signUpInput.getName());
            user.setSurname(signUpInput.getSurname());
            user.setPhone(signUpInput.getPhone());
            user.setStatus(UserStatus.WAITING);
            user.setRepresentativeEmployeeId(null);
            user.setDeleted(false);
            user.setCreateDate(date);
            user.setUpdateDate(date);
            userRepository.save(user);
            log.info(user.getId() + " is signed");
            AddRoleToUserInput addRoleToUserInput = new AddRoleToUserInput(user.getId(), Role.USER.toString());
            userRoleService.addRoleToUser(addRoleToUserInput);
            log.info(user.getId() + " ROLE.USER added ");
            walletAccountService.createWalletAccount(user.getId());
            log.info(user.getId() + " 's wallet created");


            return true;
        } else {
            return false;
        }
    }

    public UserPayload getUserById(GetUserByIdInput getUserByIdInput) {
        return mapperService.convertToUserPayload(getUserByIdInput);
    }
}
