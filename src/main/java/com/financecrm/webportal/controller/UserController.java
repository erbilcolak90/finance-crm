package com.financecrm.webportal.controller;

import com.financecrm.webportal.payload.user.UserPayload;
import com.financecrm.webportal.services.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private CustomUserService customUserService;

    @Autowired
    public UserController(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }

    @PostMapping("/getUserById")
    public ResponseEntity<UserPayload> getUserById(@RequestParam String id){
        try{
            UserPayload result = customUserService.getUserById(id);
            if(result != null){
                return ResponseEntity.ok(result);
            }else{
                throw new AccountNotFoundException("User not found");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
