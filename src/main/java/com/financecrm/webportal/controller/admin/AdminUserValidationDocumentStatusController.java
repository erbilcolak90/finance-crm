package com.financecrm.webportal.controller.admin;

import com.financecrm.webportal.entities.UserValidationDocument;
import com.financecrm.webportal.input.uservalidationdocument.UpdateUserValidationDocumentStatusInput;
import com.financecrm.webportal.services.admin.AdminUserValidationDocumentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/userValidationDocument")
public class AdminUserValidationDocumentStatusController {

    @Autowired
    private AdminUserValidationDocumentStatusService adminUserValidationDocumentStatusService;

    @PostMapping("/updateUserValidationDocumentStatus")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserValidationDocument> updateUserValidationDocumentStatus(@RequestBody UpdateUserValidationDocumentStatusInput updateUserValidationDocumentStatusInput){
        UserValidationDocument result = adminUserValidationDocumentStatusService.updateUserValidationDocumentStatus(updateUserValidationDocumentStatusInput);
        if(result != null){
            return ResponseEntity.ok(result);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
