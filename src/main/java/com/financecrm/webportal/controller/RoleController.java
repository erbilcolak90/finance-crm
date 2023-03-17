package com.financecrm.webportal.controller;

import com.financecrm.webportal.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {

    private RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/getRoleIdByRoleName")
    public ResponseEntity<String> getRoleIdByRoleName(@RequestParam String roleName) {
        String result = roleService.getRoleIdByRoleName(roleName);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Role not found",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createRole")
    public ResponseEntity<String> createRole(@RequestParam String roleName) {
        String result = roleService.createRole(roleName);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Role is already exist", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/deleteRoleByName")
    public ResponseEntity<Boolean> deleteRoleByName(String roleName) {
        if (roleService.deleteRoleByName(roleName)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

}
