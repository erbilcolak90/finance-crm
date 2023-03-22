package com.financecrm.webportal.controller;

import com.financecrm.webportal.input.role.AddRoleToUserInput;
import com.financecrm.webportal.input.role.DeleteRoleFromUserInput;
import com.financecrm.webportal.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/userRole")
@CrossOrigin
public class UserRoleController {

    private UserRoleService userRoleService;

    @Autowired
    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping("/addRoleToUser")
    public ResponseEntity<String> addRoleToUser(@RequestBody AddRoleToUserInput addRoleToUserInput) throws AccountNotFoundException {
        String result = userRoleService.addRoleToUser(addRoleToUserInput);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            throw new AccountNotFoundException("user or rolename not found");
        }
    }

    @PostMapping("/deleteRoleFromUser")
    public ResponseEntity<String> deleteRoleFromUser(@RequestBody DeleteRoleFromUserInput deleteRoleFromUserInput) throws AccountNotFoundException {
        String result = userRoleService.deleteRoleFromUser(deleteRoleFromUserInput);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            throw new AccountNotFoundException("user or rolename not found");
        }
    }

    @GetMapping("/getUserRolesByUserId")
    public ResponseEntity<List<String>> getUserRolesByUserId(@RequestBody String userId) throws AccountNotFoundException {
        List<String> result = userRoleService.getUserRolesByUserId(userId);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            throw new AccountNotFoundException("User not found");
        }
    }
}
