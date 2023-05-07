package com.financecrm.webportal.services;

import com.financecrm.webportal.auth.JwtTokenFilter;
import com.financecrm.webportal.auth.TokenManager;
import com.financecrm.webportal.entities.UserValidationDocument;
import com.financecrm.webportal.enums.UserValidationDocumentStatus;
import com.financecrm.webportal.input.uservalidationdocument.GetAllUserValidationDocumentByUserIdInput;
import com.financecrm.webportal.input.uservalidationdocument.GetUserValidationDocumentByIdInput;
import com.financecrm.webportal.input.uservalidationdocument.AddUserValidationDocumentInput;
import com.financecrm.webportal.payload.uservalidationdocument.UserValidationDocumentPayload;
import com.financecrm.webportal.repositories.UserValidationDocumentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationDocumentService {

    @Autowired
    private UserValidationDocumentRepository userValidationDocumentRepository;
    @Autowired
    private CustomUserService customUserService;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private MapperService mapperService;

    public UserValidationDocumentPayload getUserValidationDocumentById(GetUserValidationDocumentByIdInput getUserValidationDocumentByIdInput, HttpServletRequest request) {
        String token = jwtTokenFilter.getJwtFromRequest(request);
        String userIdFromToken = tokenManager.parseUserIdFromToken(token);

        UserValidationDocument db_document = userValidationDocumentRepository.findById(getUserValidationDocumentByIdInput.getId()).orElse(null);
        if (db_document != null && !db_document.isDeleted()) {
            return mapperService.convertToUserValidationDocumentPayload(db_document);
        }
        return null;
    }

    public List<UserValidationDocumentPayload> getAllUserValidationDocumentByUserId(GetAllUserValidationDocumentByUserIdInput getAllUserValidationDocumentByUserIdInput, HttpServletRequest request) {
        String token = jwtTokenFilter.getJwtFromRequest(request);
        String userIdFromToken = tokenManager.parseUserIdFromToken(token);

        if (userIdFromToken.equals(getAllUserValidationDocumentByUserIdInput.getUserId())) {
            List<UserValidationDocument> documentList = userValidationDocumentRepository.findAllByUserId(getAllUserValidationDocumentByUserIdInput.getUserId());
            return documentList.stream()
                    .map(mapperService::convertToUserValidationDocumentPayload)
                    .collect(Collectors.toList());

        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    public UserValidationDocumentPayload addUserValidationDocument(AddUserValidationDocumentInput addUserValidationDocumentInput, HttpServletRequest request) {
        String token = jwtTokenFilter.getJwtFromRequest(request);
        String userIdFromToken = tokenManager.parseUserIdFromToken(token);

        // TODO: if içerisine admin kontrolü de eklenecek. Tokendan gelen userId nin rol kontrolü yapılıp rol içerisinde admin yer alıyorsa da döküman yükleyebilir olacak. ya da bunun için @Preauthorize eklenebilir

        if (userIdFromToken.equals(addUserValidationDocumentInput.getUserId())) {
            UserValidationDocument userValidationDocument = new UserValidationDocument();
            userValidationDocument.setUserId(addUserValidationDocumentInput.getUserId());
            userValidationDocument.setUrl(addUserValidationDocumentInput.getUrl());
            userValidationDocument.setType(addUserValidationDocumentInput.getType());
            userValidationDocument.setStatus(UserValidationDocumentStatus.WAITING);
            userValidationDocument.setDeleted(false);
            Date date = new Date();
            userValidationDocument.setCreateDate(date);
            userValidationDocument.setUpdateDate(date);
            userValidationDocumentRepository.save(userValidationDocument);
            log.info("user validation document saved" + userValidationDocument.getId());

            return mapperService.convertToUserValidationDocumentPayload(userValidationDocument);

        } else {
            return null;
        }
    }
}
