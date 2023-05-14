package com.financecrm.webportal.controller;

import com.financecrm.webportal.input.uservalidationdocument.GetAllUserValidationDocumentByUserIdInput;
import com.financecrm.webportal.input.uservalidationdocument.GetUserValidationDocumentByIdInput;
import com.financecrm.webportal.input.uservalidationdocument.AddUserValidationDocumentInput;
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


    private final UserValidationDocumentService userValidationDocumentService;

    @PostMapping("/getUserValidationDocumentById")
    public ResponseEntity<UserValidationDocumentPayload> getUserValidationDocumentById(@RequestBody GetUserValidationDocumentByIdInput getUserValidationDocumentByIdInput) {
        UserValidationDocumentPayload result = userValidationDocumentService.getUserValidationDocumentById(getUserValidationDocumentByIdInput);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @PostMapping("/getAllUserValidationDocumentByUserId")
    public ResponseEntity<List<UserValidationDocumentPayload>> getAllUserValidationDocumentByUserId(@RequestBody GetAllUserValidationDocumentByUserIdInput getAllUserValidationDocumentByUserIdInput, HttpServletRequest request) {
        List<UserValidationDocumentPayload> result = userValidationDocumentService.getAllUserValidationDocumentByUserId(getAllUserValidationDocumentByUserIdInput, request);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @PostMapping("/addUserValidationDocument")
    public ResponseEntity<UserValidationDocumentPayload> addUserValidationDocument(@RequestBody AddUserValidationDocumentInput addUserValidationDocumentInput, HttpServletRequest request) {
        UserValidationDocumentPayload result = userValidationDocumentService.addUserValidationDocument(addUserValidationDocumentInput, request);
        if (result != null) {
            log.info(addUserValidationDocumentInput.getUserId() + " is added " + addUserValidationDocumentInput.getType() + " document " + Date.from(Instant.now()));
            return ResponseEntity.ok(result);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
