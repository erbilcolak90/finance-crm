package com.financecrm.webportal.controller;

import com.financecrm.webportal.input.userrole.AddRoleToUserInput;
import com.financecrm.webportal.input.userrole.DeleteRoleFromUserInput;
import com.financecrm.webportal.input.userrole.GetUserRolesByUserIdInput;
import com.financecrm.webportal.payload.role.DeleteRoleByNamePayload;
import com.financecrm.webportal.payload.userrole.AddRoleToUserPayload;
import com.financecrm.webportal.payload.userrole.DeleteRoleFromUserPayload;
import com.financecrm.webportal.services.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/userRole")
@RequiredArgsConstructor
@CrossOrigin
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/addRoleToUser")
    public ResponseEntity<AddRoleToUserPayload> addRoleToUser(@RequestBody AddRoleToUserInput addRoleToUserInput) throws AccountNotFoundException {
        AddRoleToUserPayload result = userRoleService.addRoleToUser(addRoleToUserInput);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            throw new AccountNotFoundException("user or rolename not found");
        }
    }

    @PostMapping("/deleteRoleFromUser")
    public ResponseEntity<DeleteRoleFromUserPayload> deleteRoleFromUser(@RequestBody DeleteRoleFromUserInput deleteRoleFromUserInput) throws AccountNotFoundException {
        DeleteRoleFromUserPayload result = userRoleService.deleteRoleFromUser(deleteRoleFromUserInput);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            throw new AccountNotFoundException("user or rolename not found");
        }
    }

    @GetMapping("/getUserRolesByUserId")
    public ResponseEntity<List<String>> getUserRolesByUserId(@RequestBody GetUserRolesByUserIdInput getUserRolesByUserIdInput) throws AccountNotFoundException {
        List<String> result = userRoleService.getUserRolesByUserId(getUserRolesByUserIdInput);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            throw new AccountNotFoundException("User not found");
        }
    }
}
