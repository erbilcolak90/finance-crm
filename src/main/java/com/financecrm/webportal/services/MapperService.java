package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.UserValidationDocument;
import com.financecrm.webportal.payload.uservalidationdocument.UserValidationDocumentPayload;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperService {

    private final ModelMapper modelMapper = new ModelMapper();

    public UserValidationDocumentPayload convertFromUserValidationDocument(UserValidationDocument userValidationDocument){
        return modelMapper.map(userValidationDocument,UserValidationDocumentPayload.class);
    }
}
