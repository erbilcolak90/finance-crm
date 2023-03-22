package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.Role;
import com.financecrm.webportal.input.PaginationInput;
import com.financecrm.webportal.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public String findById(String roleId){
        Role role = roleRepository.findById(roleId).orElse(null);
        if(role != null){
            return role.getName();
        }else{
            return null;
        }
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

    public List<Role> getAllRoles(PaginationInput paginationInput) {
        // TODO: pagination - replaced with List<Role>
        // Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(), Sort.by(Sort.Direction.valueOf(pagination.getSortBy().toString()), pagination.getFieldName()));
        return roleRepository.findAll();
    }

}
