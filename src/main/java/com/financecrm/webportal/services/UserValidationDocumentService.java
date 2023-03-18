package com.financecrm.webportal.services;

import com.financecrm.webportal.auth.JwtTokenFilter;
import com.financecrm.webportal.auth.TokenManager;
import com.financecrm.webportal.entities.UserValidationDocument;
import com.financecrm.webportal.enums.UserValidationDocumentStatus;
import com.financecrm.webportal.input.uservalidationdocument.UserValidationDocumentInput;
import com.financecrm.webportal.payload.uservalidationdocument.UserValidationDocumentPayload;
import com.financecrm.webportal.repositories.UserValidationDocumentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserValidationDocumentService {

    private UserValidationDocumentRepository userValidationDocumentRepository;
    private CustomUserService customUserService;
    private JwtTokenFilter jwtTokenFilter;
    private TokenManager tokenManager;
    private MapperService mapperService;

    @Autowired
    public UserValidationDocumentService(UserValidationDocumentRepository userValidationDocumentRepository, CustomUserService customUserService, JwtTokenFilter jwtTokenFilter, TokenManager tokenManager, MapperService mapperService) {
        this.userValidationDocumentRepository = userValidationDocumentRepository;
        this.customUserService = customUserService;
        this.jwtTokenFilter = jwtTokenFilter;
        this.tokenManager = tokenManager;
        this.mapperService = mapperService;
    }

    public UserValidationDocumentPayload getUserValidationDocumentById(String userValidationDocumentId, HttpServletRequest request){

        UserValidationDocument db_document = userValidationDocumentRepository.findById(userValidationDocumentId).orElse(null);
        if(db_document != null && !db_document.isDeleted()){
            return mapperService.convertFromUserValidationDocument(db_document);
        }
        return null;
    }

    public List<UserValidationDocumentPayload> getAllUserValidationDocumentByUserId(String userId, HttpServletRequest request){
        String token = jwtTokenFilter.getJwtFromRequest(request);
        String userIdFromToken = tokenManager.parseUserIdFromToken(token);

        if(userIdFromToken.equals(userId)){
            List<UserValidationDocument> documentList = userValidationDocumentRepository.findAllByUserId(userId);
            return documentList.stream()
                    .map(mapperService::convertFromUserValidationDocument)
                    .collect(Collectors.toList());

        }
        return null;
    }

    @Transactional
    public UserValidationDocumentPayload addUserValidationDocument(UserValidationDocumentInput userValidationDocumentInput, HttpServletRequest request){
       String token = jwtTokenFilter.getJwtFromRequest(request);
       String userIdFromToken = tokenManager.parseUserIdFromToken(token);

       // TODO: if içerisine admin kontrolü de eklenecek. Tokendan gelen userId nin rol kontrolü yapılıp rol içerisinde admin yer alıyorsa da döküman yükleyebilir olacak. ya da bunun için @Preauthorize eklenebilir

       if(userIdFromToken.equals(userValidationDocumentInput.getUserId())){
           UserValidationDocument userValidationDocument = new UserValidationDocument();
           userValidationDocument.setUserId(userValidationDocumentInput.getUserId());
           userValidationDocument.setUrl(userValidationDocumentInput.getUrl());
           userValidationDocument.setType(userValidationDocumentInput.getType());
           userValidationDocument.setStatus(UserValidationDocumentStatus.WAITING);
           userValidationDocument.setDeleted(false);
           Date date = new Date();
           userValidationDocument.setCreateDate(date);
           userValidationDocument.setUpdateDate(date);
           userValidationDocumentRepository.save(userValidationDocument);

           return mapperService.convertFromUserValidationDocument(userValidationDocument);

       }else{
           return null;
       }

    }
}
