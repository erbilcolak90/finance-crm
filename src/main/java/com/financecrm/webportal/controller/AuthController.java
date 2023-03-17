package com.financecrm.webportal.controller;

import com.financecrm.webportal.auth.TokenManager;
import com.financecrm.webportal.input.LoginInput;
import com.financecrm.webportal.input.UserInput;
import com.financecrm.webportal.payload.LoginPayload;
import com.financecrm.webportal.services.CustomUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginInput loginInput) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginInput.getEmail(),loginInput.getPassword()));
            if(authentication.isAuthenticated()){
                String token = tokenManager.generateToken(loginInput.getEmail());
                String userId = tokenManager.parseUserIdFromToken(token);
                LoginPayload loginPayload = new LoginPayload(token,userId);

                return new ResponseEntity<>(loginPayload, HttpStatus.OK);
            }
        }catch (AuthenticationException e){

            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);

        }
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        if(tokenManager.tokenValidate(token)){
            tokenManager.logout(token);

            return new ResponseEntity<>(true,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody UserInput userInput) {

        if(customUserService.signUp(userInput)){
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("This user is already exist",HttpStatus.BAD_REQUEST);
        }

    }
}
