package com.financecrm.webportal.controller;

import com.financecrm.webportal.entities.UserRole;
import com.financecrm.webportal.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    public ResponseEntity<?> addRoleToUser(@RequestParam String userId, @RequestParam String roleName) {

        String result = userRoleService.addRoleToUser(userId, roleName);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User or Role not found",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/deleteRoleFromUser")
    public ResponseEntity<?> deleteRoleFromUser(@RequestParam String userId,@RequestParam String roleName){
        String result = userRoleService.deleteRoleFromUser(userId, roleName);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User or Role not found",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getUserRolesByUserId")
    public ResponseEntity<?> getUserRolesByUserId(String userId){
        List<UserRole> result = userRoleService.getUserRolesByUserId(userId);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found",HttpStatus.BAD_REQUEST);
        }
    }
}
