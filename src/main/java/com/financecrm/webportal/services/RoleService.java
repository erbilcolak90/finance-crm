package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.Role;
import com.financecrm.webportal.input.PaginationInput;
import com.financecrm.webportal.input.role.CreateRoleInput;
import com.financecrm.webportal.input.role.DeleteRoleByNameInput;
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
        if (role != null) {
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
            roleRepository.save(role);
            log.info(createRoleInput.getRoleName() + " saved");
            return new CreateRolePayload(createRoleInput.getRoleName() + " role created");
        } else {
            return null;
        }
    }

    @Transactional
    public DeleteRoleByNamePayload deleteRoleByName(DeleteRoleByNameInput deleteRoleByNameInput) {
        Role db_role = roleRepository.findByName(deleteRoleByNameInput.getRoleName()).orElse(null);
        if (db_role != null) {
            db_role.setDeleted(false);
            roleRepository.save(db_role);
            return new DeleteRoleByNamePayload(true);
        } else {
            log.info("role is not found");
            return new DeleteRoleByNamePayload(false);
        }
    }

    public GetRoleIdByRoleNamePayload getRoleIdByRoleName(GetRoleIdByRoleNameInput getRoleIdByRoleNameInput) {
        Role role = roleRepository.findByName(getRoleIdByRoleNameInput.getRoleName()).orElse(null);
        if (role != null) {
            return new GetRoleIdByRoleNamePayload(role.getId());
        } else {
            return null;
        }
    }

    public Page<Role> getAllRoles(PaginationInput paginationInput) {
        Pageable pageable = PageRequest.of(paginationInput.getPage(), paginationInput.getSize(), Sort.by(Sort.Direction.valueOf(paginationInput.getSortBy().toString()), paginationInput.getFieldName()));
        return roleRepository.findByIsDeletedFalse(pageable);
    }

}
