package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.entities.UserRole;
import com.financecrm.webportal.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserRoleService {

    private UserRoleRepository userRoleRepository;
    private RoleService roleService;
    private CustomUserService customUserService;

    @Autowired
    public UserRoleService(UserRoleRepository userRoleRepository, RoleService roleService, CustomUserService customUserService) {
        this.userRoleRepository = userRoleRepository;
        this.roleService = roleService;
        this.customUserService = customUserService;
    }

    public List<String> getUserRolesByUserId(String userId){
        User user = customUserService.findByUserId(userId);
        if(user != null){
            return userRoleRepository.getUserRolesByUserId(userId);
        }else{
            return null;
        }


    }

    @Transactional
    public String addRoleToUser(String userId, String roleName){
        User user = customUserService.findByUserId(userId);
        String roleId = roleService.getRoleIdByRoleName(roleName);
        List<UserRole> userRoles = userRoleRepository.findByUserIdAndRoleId(userId,roleId);
        if (user != null && roleId != null) {
            if(userRoles.stream().anyMatch(userRole -> userRole.getRoleId().equals(roleId))){
                return "User is already has this role";
            }
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleId);
            userRoleRepository.save(userRole);

            return roleName+ " added to " + userId;
        } else {
            return "User or rolename not found";
        }
    }

    @Transactional
    public String deleteRoleFromUser(String userId, String roleName){
        User user = customUserService.findByUserId(userId);
        String roleId = roleService.getRoleIdByRoleName(roleName);
        List<UserRole> userRoles = userRoleRepository.findByUserIdAndRoleId(userId,roleId);
        if(user != null && roleId != null){
            userRoles.stream().filter(userRole -> userRole.getRoleId().equals(roleId))
                    .forEach(userRole -> {
                        userRole.setDeleted(true);
                        userRoleRepository.save(userRole);
                    });

            return "Role deleted from user";
        }else{
            return "User or rolename not found";
        }
    }
}
