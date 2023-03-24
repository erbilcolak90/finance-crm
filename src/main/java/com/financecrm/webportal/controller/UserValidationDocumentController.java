package com.financecrm.webportal.controller;

import com.financecrm.webportal.input.uservalidationdocument.UserValidationDocumentInput;
import com.financecrm.webportal.payload.uservalidationdocument.UserValidationDocumentPayload;
import com.financecrm.webportal.services.UserValidationDocumentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/userValidationDocument")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
public class UserValidationDocumentController {

    @Autowired
    private UserValidationDocumentService userValidationDocumentService;

    @GetMapping("/getUserValidationDocumentById")
    public ResponseEntity<UserValidationDocumentPayload> getUserValidationDocumentById(@RequestParam String userValidationDocumentId, HttpServletRequest request) {
        UserValidationDocumentPayload result = userValidationDocumentService.getUserValidationDocumentById(userValidationDocumentId, request);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @PostMapping("/getAllUserValidationDocumentByUserId")
    public ResponseEntity<List<UserValidationDocumentPayload>> getAllUserValidationDocumentByUserId(@RequestParam String userId, HttpServletRequest request) {
        List<UserValidationDocumentPayload> result = userValidationDocumentService.getAllUserValidationDocumentByUserId(userId, request);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @PostMapping("/addUserValidationDocument")
    public ResponseEntity<UserValidationDocumentPayload> addUserValidationDocument(@RequestBody UserValidationDocumentInput userValidationDocumentInput, HttpServletRequest request) {
        UserValidationDocumentPayload result = userValidationDocumentService.addUserValidationDocument(userValidationDocumentInput, request);
        if (result != null) {
            log.info(userValidationDocumentInput.getUserId() + " is added " + userValidationDocumentInput.getType() + " document " + Date.from(Instant.now()));
            return ResponseEntity.ok(result);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
