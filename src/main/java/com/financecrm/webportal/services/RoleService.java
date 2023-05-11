package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.Role;
import com.financecrm.webportal.input.PaginationInput;
import com.financecrm.webportal.input.role.CreateRoleInput;
import com.financecrm.webportal.input.role.DeleteRoleByNameInput;
import com.financecrm.webportal.input.role.GetAllRolesInput;
import com.financecrm.webportal.input.role.GetRoleIdByRoleNameInput;
import com.financecrm.webportal.payload.role.CreateRolePayload;
import com.financecrm.webportal.payload.role.DeleteRoleByNamePayload;
import com.financecrm.webportal.payload.role.GetRoleIdByRoleNamePayload;
import com.financecrm.webportal.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public String findById(String roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role != null && !role.isDeleted()) {
            return role.getName();
        } else {
            return null;
        }
    }

    @Transactional
    public CreateRolePayload createRole(CreateRoleInput createRoleInput) {
        Role db_role = roleRepository.findByName(createRoleInput.getRoleName()).orElse(null);
        if (db_role == null) {
            Role role = new Role();
            role.setName(createRoleInput.getRoleName());
            role.setDeleted(false);
            Role savedRole = roleRepository.save(role);
            log.info(savedRole.getName() + " saved");
            return new CreateRolePayload(savedRole.getName() + " role created");
        } else if (db_role.isDeleted()) {
            db_role.setName(createRoleInput.getRoleName());
            db_role.setDeleted(false);
            Role savedRole = roleRepository.save(db_role);
            log.info(savedRole.getName() + " role can usable");
            return new CreateRolePayload(savedRole.getName() + " role can usable");
        } else {
            return null;
        }
    }

    @Transactional
    public DeleteRoleByNamePayload deleteRoleByName(DeleteRoleByNameInput deleteRoleByNameInput) {
        Role db_role = roleRepository.findByName(deleteRoleByNameInput.getRoleName()).orElse(null);
        if (db_role != null && !db_role.isDeleted()) {
            db_role.setDeleted(true);
            roleRepository.save(db_role);
            return new DeleteRoleByNamePayload(true);
        } else {
            log.info("role is not found or is already deleted");
            return new DeleteRoleByNamePayload(false);
        }
    }

    public GetRoleIdByRoleNamePayload getRoleIdByRoleName(GetRoleIdByRoleNameInput getRoleIdByRoleNameInput) {
        Role role = roleRepository.findByName(getRoleIdByRoleNameInput.getRoleName()).orElse(null);
        if (role != null && !role.isDeleted()) {
            return new GetRoleIdByRoleNamePayload(role.getId());
        } else {
            return null;
        }
    }

    public Page<Role> getAllRoles(GetAllRolesInput getAllRolesInput) {
        Pageable pageable = PageRequest.of(getAllRolesInput.getPaginationInput().getPage(), getAllRolesInput.getPaginationInput().getSize(), Sort.by(Sort.Direction.valueOf(getAllRolesInput.getPaginationInput().getSortBy().toString()), getAllRolesInput.getPaginationInput().getFieldName()));
        return roleRepository.findByIsDeletedFalse(pageable);
    }

}
