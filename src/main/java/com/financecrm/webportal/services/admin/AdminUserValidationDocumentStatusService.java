package com.financecrm.webportal.services.admin;

import com.financecrm.webportal.entities.UserValidationDocument;
import com.financecrm.webportal.input.uservalidationdocument.UpdateUserValidationDocumentStatusInput;
import com.financecrm.webportal.repositories.UserValidationDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AdminUserValidationDocumentStatusService {

    @Autowired
    private UserValidationDocumentRepository userValidationDocumentRepository;

    @Transactional
    public UserValidationDocument updateUserValidationDocumentStatus(UpdateUserValidationDocumentStatusInput updateUserValidationDocumentStatusInput){
        UserValidationDocument userValidationDocument = userValidationDocumentRepository.findById(updateUserValidationDocumentStatusInput.getDocumentId()).orElse(null);
        if(userValidationDocument != null){
            userValidationDocument.setStatus(updateUserValidationDocumentStatusInput.getStatus());
            userValidationDocument.setUpdateDate(new Date());
            userValidationDocumentRepository.save(userValidationDocument);

            return userValidationDocument;
        }
        else{
        return null ;
        }
    }
}
