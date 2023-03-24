package com.financecrm.webportal.controller;

import com.financecrm.webportal.auth.TokenManager;
import com.financecrm.webportal.input.login.LoginInput;
import com.financecrm.webportal.input.user.UserInput;
import com.financecrm.webportal.payload.auth.LoginPayload;
import com.financecrm.webportal.payload.auth.LogoutPayload;
import com.financecrm.webportal.payload.auth.SignUpPayload;
import com.financecrm.webportal.services.CustomUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin
@RequiredArgsConstructor
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
    public ResponseEntity<LoginPayload> login(@RequestBody LoginInput loginInput) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginInput.getEmail(), loginInput.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = tokenManager.generateToken(loginInput.getEmail());
                String userId = tokenManager.parseUserIdFromToken(token);
                LoginPayload loginPayload = new LoginPayload(token, userId);
                log.info(loginInput.getEmail() + " login at " + Date.from(Instant.now()));
                return ResponseEntity.ok(loginPayload);
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutPayload> logout(HttpServletRequest request) {
        try {
            String header = request.getHeader("Authorization");
            String token = header.substring(7);
            if (tokenManager.tokenValidate(token)) {
                String userId = tokenManager.parseUserIdFromToken(token);
                tokenManager.logout(token);
                log.info(userId + " logout at " + Date.from(Instant.now()));
                return ResponseEntity.ok(new LogoutPayload(true));
            } else {
                log.info("token invalid");
                return ResponseEntity.ok(new LogoutPayload(false));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/signUp")
    public ResponseEntity<SignUpPayload> signUp(@RequestBody UserInput userInput) throws BadCredentialsException {
        try {
            if (customUserService.signUp(userInput)) {
                log.info(userInput.getEmail() + " is signed " + Date.from(Instant.now()));
                return ResponseEntity.ok(new SignUpPayload(true));
            } else {
                throw new BadCredentialsException("This user is already exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
