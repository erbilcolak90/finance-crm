package com.financecrm.webportal.controller;

import com.financecrm.webportal.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@CrossOrigin
public class RoleController {

    private RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/getRoleIdByRoleName")
    public ResponseEntity<String> getRoleIdByRoleName(@RequestBody String roleName) {
        try {
            String result = roleService.getRoleIdByRoleName(roleName);
            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                throw new BadCredentialsException("Role not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/createRole")
    public ResponseEntity<String> createRole(@RequestBody String roleName) {
        try {
            String result = roleService.createRole(roleName);
            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                throw new IllegalArgumentException("Role is already exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/deleteRoleByName")
    public ResponseEntity<Boolean> deleteRoleByName(@RequestBody String roleName) {
        try{
            if (roleService.deleteRoleByName(roleName)) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
