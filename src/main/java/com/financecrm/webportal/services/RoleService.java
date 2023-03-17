package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.Role;
import com.financecrm.webportal.input.Pagination;
import com.financecrm.webportal.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public String createRole(String roleName) {
        Role db_role = roleRepository.findByName(roleName);
        if (db_role == null) {
            Role role = new Role();
            role.setName(roleName);
            role.setDeleted(false);
            roleRepository.save(role);
            return roleName + " role created";
        } else {
            return null;
        }
    }

    @Transactional
    public Boolean deleteRoleByName(String roleName) {
        Role db_role = roleRepository.findByName(roleName);
        if (db_role != null) {
            db_role.setDeleted(false);
            roleRepository.save(db_role);
            return true;
        } else {
            return false;
        }
    }

    public String getRoleIdByRoleName(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role != null) {
            return role.getId();
        } else {
            return null;
        }
    }

    public List<Role> getAllRoles(Pagination pagination) {
        // TODO: pagination - replaced with List<Role>
        // Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(), Sort.by(Sort.Direction.valueOf(pagination.getSortBy().toString()), pagination.getFieldName()));
        return roleRepository.findAll();
    }

}
