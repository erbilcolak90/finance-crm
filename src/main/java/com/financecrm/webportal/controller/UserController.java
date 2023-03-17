package com.financecrm.webportal.controller;

import com.financecrm.webportal.payload.UserPayload;
import com.financecrm.webportal.services.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        UserPayload result = customUserService.getUserById(id);
        if(result != null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

}
