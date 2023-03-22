package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.entities.UserRole;
import com.financecrm.webportal.input.role.AddRoleToUserInput;
import com.financecrm.webportal.input.role.DeleteRoleFromUserInput;
import com.financecrm.webportal.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CustomUserService customUserService;

    public List<String> getUserRolesByUserId(String userId) {
        User user = customUserService.findByUserId(userId);
        if (user != null) {
            List<UserRole> roleList = userRoleRepository.getUserRolesByUserId(userId);
            List<String> roleNameList = new ArrayList<>();

            for (UserRole role : roleList) {
                String roleName = roleService.findById(role.getRoleId());
                roleNameList.add(roleName);
            }
            return roleNameList;
        } else {
            return null;
        }
    }

    @Transactional
    public String addRoleToUser(AddRoleToUserInput addRoleToUserInput) {
        User user = customUserService.findByUserId(addRoleToUserInput.getUserId());
        String roleId = roleService.getRoleIdByRoleName(addRoleToUserInput.getRoleName());
        List<UserRole> userRoles = userRoleRepository.findByUserIdAndRoleId(addRoleToUserInput.getUserId(), roleId);
        if (user != null && roleId != null) {
            if (userRoles.stream().anyMatch(userRole -> userRole.getRoleId().equals(roleId))) {
                return "User is already has this role";
            }
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleId);
            userRoleRepository.save(userRole);
            log.info(userRole.getRoleId() + " role is added to user " + userRole.getUserId());
            return addRoleToUserInput.getRoleName() + " added to " + addRoleToUserInput.getUserId();
        } else {
            return "User or rolename not found";
        }
    }

    @Transactional
    public String deleteRoleFromUser(DeleteRoleFromUserInput deleteRoleFromUserInput) {
        User user = customUserService.findByUserId(deleteRoleFromUserInput.getUserId());
        String roleId = roleService.getRoleIdByRoleName(deleteRoleFromUserInput.getRoleName());
        List<UserRole> userRoles = userRoleRepository.findByUserIdAndRoleId(deleteRoleFromUserInput.getUserId(), deleteRoleFromUserInput.getRoleName());
        if (user != null && roleId != null) {
            userRoles.stream().filter(userRole -> userRole.getRoleId().equals(roleId))
                    .forEach(userRole -> {
                        userRole.setDeleted(true);
                        userRoleRepository.save(userRole);
                        log.info(userRole.getRoleId() + " deleted from "+ userRole.getUserId());
                    });

            return "Role deleted from user";
        } else {
            return "User or rolename not found";
        }
    }
}
