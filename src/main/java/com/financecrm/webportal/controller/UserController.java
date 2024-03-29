package com.financecrm.webportal.controller;

import com.financecrm.webportal.input.user.GetUserByIdInput;
import com.financecrm.webportal.payload.user.UserPayload;
import com.financecrm.webportal.services.CustomUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private CustomUserService customUserService;

    @PostMapping("/getUserById")
    public ResponseEntity<UserPayload> getUserById(@RequestBody GetUserByIdInput getUserByIdInput){
        try{
            UserPayload result = customUserService.getUserById(getUserByIdInput);
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
