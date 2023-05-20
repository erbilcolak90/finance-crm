package com.financecrm.webportal.services.admin;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.input.user.UpdateUserStatusInput;
import com.financecrm.webportal.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AdminCustomUserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User updateUserStatus(UpdateUserStatusInput updateUserStatusInput){
        User user = userRepository.findById(updateUserStatusInput.getUserId()).orElse(null);
        if(user != null){
            user.setStatus(updateUserStatusInput.getStatus());
            user.setUpdateDate(new Date());
            User savedUser = userRepository.save(user);
            return savedUser;
        }
        else{
            return null;
        }
    }
}
