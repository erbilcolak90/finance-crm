package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.entities.UserRole;
import com.financecrm.webportal.input.userrole.AddRoleToUserInput;
import com.financecrm.webportal.input.userrole.DeleteRoleFromUserInput;
import com.financecrm.webportal.input.role.GetRoleIdByRoleNameInput;
import com.financecrm.webportal.input.userrole.GetUserRolesByUserIdInput;
import com.financecrm.webportal.payload.role.DeleteRoleByNamePayload;
import com.financecrm.webportal.payload.role.GetRoleIdByRoleNamePayload;
import com.financecrm.webportal.payload.userrole.AddRoleToUserPayload;
import com.financecrm.webportal.payload.userrole.DeleteRoleFromUserPayload;
import com.financecrm.webportal.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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

    public List<String> getUserRolesByUserId(GetUserRolesByUserIdInput getUserRolesByUserIdInput) {
        User user = customUserService.findByUserId(getUserRolesByUserIdInput.getUserId());
        if (user != null) {
            List<UserRole> roleList = userRoleRepository.getUserRolesByUserId(getUserRolesByUserIdInput.getUserId());
            List<String> roleNameList = new ArrayList<>();

            for (UserRole role : roleList) {
                String roleName = roleService.findById(role.getRoleId());
                roleNameList.add(roleName);
            }
            return roleNameList;
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    public AddRoleToUserPayload addRoleToUser(AddRoleToUserInput addRoleToUserInput) {
        User user = customUserService.findByUserId(addRoleToUserInput.getUserId());
        GetRoleIdByRoleNameInput getRoleIdByRoleNameInput = new GetRoleIdByRoleNameInput(addRoleToUserInput.getRoleName());
        GetRoleIdByRoleNamePayload roleIdPayload = roleService.getRoleIdByRoleName(getRoleIdByRoleNameInput);
        List<UserRole> userRoles = userRoleRepository.findByUserIdAndRoleId(addRoleToUserInput.getUserId(), roleIdPayload.getRoleName());
        if (user != null && roleIdPayload.getRoleName() != null) {
            if (userRoles.stream().anyMatch(userRole -> userRole.getRoleId().equals(roleIdPayload.getRoleName()))) {
                return new AddRoleToUserPayload("User is already has this role");
            }
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleIdPayload.getRoleName());
            userRoleRepository.save(userRole);
            log.info(userRole.getRoleId() + " role is added to user " + userRole.getUserId());
            return new AddRoleToUserPayload(addRoleToUserInput.getRoleName() + " added to " + addRoleToUserInput.getUserId());
        } else {
            return new AddRoleToUserPayload("User or rolename not found");
        }
    }

    @Transactional
    public DeleteRoleFromUserPayload deleteRoleFromUser(DeleteRoleFromUserInput deleteRoleFromUserInput) {
        User user = customUserService.findByUserId(deleteRoleFromUserInput.getUserId());
        GetRoleIdByRoleNameInput getRoleIdByRoleNameInput = new GetRoleIdByRoleNameInput(deleteRoleFromUserInput.getRoleName());
        GetRoleIdByRoleNamePayload roleIdPayload = roleService.getRoleIdByRoleName(getRoleIdByRoleNameInput);
        List<UserRole> userRoles = userRoleRepository.findByUserIdAndRoleId(deleteRoleFromUserInput.getUserId(), deleteRoleFromUserInput.getRoleName());
        if (user != null && roleIdPayload.getRoleName() != null) {
            userRoles.stream().filter(userRole -> userRole.getRoleId().equals(roleIdPayload.getRoleName()))
                    .forEach(userRole -> {
                        userRole.setDeleted(true);
                        userRoleRepository.save(userRole);
                        log.info(userRole.getRoleId() + " deleted from " + userRole.getUserId());
                    });

            return new DeleteRoleFromUserPayload(true);
        } else {
            log.info("user or role not found");
            return new DeleteRoleFromUserPayload(false);
        }
    }
}
