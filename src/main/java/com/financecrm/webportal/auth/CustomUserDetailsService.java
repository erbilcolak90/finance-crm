package com.financecrm.webportal.auth;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.repositories.RoleRepository;
import com.financecrm.webportal.repositories.UserRepository;
import com.financecrm.webportal.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;

    //This method connects to the DB to authenticate the user
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user;

        if(email.contains(".com")){
            user = userRepository.findByEmail(email);
        }
        else{
            user = userRepository.findById(email).orElse(null);
        }

        // Security User for the user granted authority and another UserDetails implementation.
        return new SecurityUser(user, userRoleRepository, roleRepository);
    }
}
