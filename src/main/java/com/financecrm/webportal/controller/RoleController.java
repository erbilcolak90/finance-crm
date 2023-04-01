package com.financecrm.webportal.controller;

import com.financecrm.webportal.input.role.CreateRoleInput;
import com.financecrm.webportal.input.role.DeleteRoleByNameInput;
import com.financecrm.webportal.input.role.GetRoleIdByRoleNameInput;
import com.financecrm.webportal.payload.role.CreateRolePayload;
import com.financecrm.webportal.payload.role.DeleteRoleByNamePayload;
import com.financecrm.webportal.payload.role.GetRoleIdByRoleNamePayload;
import com.financecrm.webportal.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/role")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/getRoleIdByRoleName")
    public ResponseEntity<GetRoleIdByRoleNamePayload> getRoleIdByRoleName(@RequestBody GetRoleIdByRoleNameInput getRoleIdByRoleNameInput) {
        try {
            GetRoleIdByRoleNamePayload result = roleService.getRoleIdByRoleName(getRoleIdByRoleNameInput);
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
    public ResponseEntity<CreateRolePayload> createRole(@RequestBody CreateRoleInput createRoleInput) {
        try {
            CreateRolePayload result = roleService.createRole(createRoleInput);
            if (result != null) {
                log.info(createRoleInput.getRoleName() + " role created " + Date.from(Instant.now()));
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
    public ResponseEntity<DeleteRoleByNamePayload> deleteRoleByName(@RequestBody DeleteRoleByNameInput deleteRoleByNameInput) {
        try{
            DeleteRoleByNamePayload payload = roleService.deleteRoleByName(deleteRoleByNameInput);
            if (payload.isStatus()) {
                log.info(deleteRoleByNameInput.getRoleName() + " role deleted " + Date.from(Instant.now()));
                return ResponseEntity.ok(new DeleteRoleByNamePayload(true));
            } else {
                return ResponseEntity.ok(new DeleteRoleByNamePayload(false));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
