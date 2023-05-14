package com.financecrm.webportal;

import com.financecrm.webportal.auth.JwtTokenFilter;
import com.financecrm.webportal.auth.TokenManager;
import com.financecrm.webportal.entities.UserValidationDocument;
import com.financecrm.webportal.enums.UserValidationDocumentStatus;
import com.financecrm.webportal.enums.UserValidationDocumentType;
import com.financecrm.webportal.input.uservalidationdocument.AddUserValidationDocumentInput;
import com.financecrm.webportal.input.uservalidationdocument.GetAllUserValidationDocumentByUserIdInput;
import com.financecrm.webportal.input.uservalidationdocument.GetUserValidationDocumentByIdInput;
import com.financecrm.webportal.payload.uservalidationdocument.UserValidationDocumentPayload;
import com.financecrm.webportal.repositories.UserValidationDocumentRepository;
import com.financecrm.webportal.services.CustomUserService;
import com.financecrm.webportal.services.MapperService;
import com.financecrm.webportal.services.UserValidationDocumentService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserValidationDocumentServiceTest {

    @InjectMocks
    private UserValidationDocumentService userValidationDocumentService;
    @Mock
    private UserValidationDocumentRepository userValidationDocumentRepository;
    @Mock
    private CustomUserService customUserService;
    @Mock
    private JwtTokenFilter jwtTokenFilter;
    @Mock
    private TokenManager tokenManager;
    @Mock
    private MapperService mapperService;
    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("should Return UserValidationDocumentPayload when DocumentId Is Exist In UserValidationDocument And IsDeleted False From GetUserValidationDocumentInput")
    @Tag("getUserValidationDocumentById")
    @Test
    void shouldReturnUserValidationDocumentPayload_whenDocumentIdIsExistInUserValidationDocumentAndIsDeletedFalseFromGetUserValidationDocumentInput(){
        GetUserValidationDocumentByIdInput getUserValidationDocumentByIdInput = new GetUserValidationDocumentByIdInput("test_userValidationDocumentId");
        UserValidationDocument userValidationDocument = new UserValidationDocument("test_userValidationDocumentId","test_userId","test_url", UserValidationDocumentType.TEST, UserValidationDocumentStatus.TEST,false,new Date(),new Date());
        UserValidationDocumentPayload userValidationDocumentPayload = new UserValidationDocumentPayload("test_userValidationDocumentId","test_url",UserValidationDocumentType.TEST,UserValidationDocumentStatus.TEST,userValidationDocument.getCreateDate());

        Mockito.when(userValidationDocumentRepository.findById(getUserValidationDocumentByIdInput.getId())).thenReturn(Optional.of(userValidationDocument));
        Mockito.when(mapperService.convertToUserValidationDocumentPayload(userValidationDocument)).thenReturn(userValidationDocumentPayload);

        UserValidationDocumentPayload expectedResult = userValidationDocumentService.getUserValidationDocumentById(getUserValidationDocumentByIdInput);

        assertEquals(expectedResult,userValidationDocumentPayload);

        Mockito.verify(userValidationDocumentRepository).findById(getUserValidationDocumentByIdInput.getId());
        Mockito.verify(mapperService).convertToUserValidationDocumentPayload(userValidationDocument);

    }

    @DisplayName("should Return UserValidationDocumentPayload when DocumentId Is Exist In UserValidationDocument And IsDeleted True From GetUserValidationDocumentInput")
    @Tag("getUserValidationDocumentById")
    @Test
    void shouldReturnNull_whenDocumentIdIsExistInUserValidationDocumentAndIsDeletedTrueFromGetUserValidationDocumentByIdInput(){
        GetUserValidationDocumentByIdInput getUserValidationDocumentByIdInput = new GetUserValidationDocumentByIdInput("test_userValidationDocumentId");
        UserValidationDocument userValidationDocument = new UserValidationDocument("test_userValidationDocumentId","test_userId","test_url", UserValidationDocumentType.TEST, UserValidationDocumentStatus.TEST,false,new Date(),new Date());

        Mockito.when(userValidationDocumentRepository.findById(getUserValidationDocumentByIdInput.getId())).thenReturn(Optional.of(userValidationDocument));

        UserValidationDocumentPayload expectedResult = userValidationDocumentService.getUserValidationDocumentById(getUserValidationDocumentByIdInput);

        assertNull(expectedResult);

        Mockito.verify(userValidationDocumentRepository).findById(getUserValidationDocumentByIdInput.getId());
    }

    @DisplayName("should Return UserValidationDocumentPayload when DocumentId Does Not Exist In UserValidationDocument From GetUserValidationDocumentInput")
    @Tag("getUserValidationDocumentById")
    @Test
    void shouldReturnNull_whenDocumentIdDoesNotExistInUserValidationDocumentFromGetUserValidationDocumentByIdInput(){
        GetUserValidationDocumentByIdInput getUserValidationDocumentByIdInput = new GetUserValidationDocumentByIdInput("test_userValidationDocumentId");

        Mockito.when(userValidationDocumentRepository.findById(getUserValidationDocumentByIdInput.getId())).thenReturn(Optional.empty());

        UserValidationDocumentPayload expectedResult = userValidationDocumentService.getUserValidationDocumentById(getUserValidationDocumentByIdInput);

        assertNull(expectedResult);

        Mockito.verify(userValidationDocumentRepository).findById(getUserValidationDocumentByIdInput.getId());
    }

    @DisplayName("should Return List UserDocumentPayload when UserId Is Exist In Token From Request And UserId From Token Equals UserId From GetAllUserValidationDocumentByUserIdInput")
    @Tag("getAllUserValidationDocumentByUserId")
    @Test
    void shouldReturnListUserDocumentPayload_whenUserIdIsExistInTokenFromRequestAndUserIdFromTokenEqualsUserIdFromGetAllUserValidationDocumentByUserIdInput(){
        GetAllUserValidationDocumentByUserIdInput getAllUserValidationDocumentByUserIdInput = new GetAllUserValidationDocumentByUserIdInput("test_userId");
        String token = "Bearer test_userId";
        String userId = "test_userId";
        UserValidationDocument userValidationDocument = new UserValidationDocument("test_userValidationDocumentId","test_userId","test_url", UserValidationDocumentType.TEST, UserValidationDocumentStatus.TEST,false,new Date(),new Date());
        List<UserValidationDocument> documentList = new ArrayList<>();
        documentList.add(userValidationDocument);
        UserValidationDocumentPayload userValidationDocumentPayload = new UserValidationDocumentPayload("test_userValidationDocumentId","test_url",UserValidationDocumentType.TEST,UserValidationDocumentStatus.TEST,userValidationDocument.getCreateDate());

        Mockito.when(jwtTokenFilter.getJwtFromRequest(httpServletRequest)).thenReturn(token);
        Mockito.when(tokenManager.parseUserIdFromToken(token)).thenReturn(userId);
        Mockito.when(userValidationDocumentRepository.findAllByUserId(getAllUserValidationDocumentByUserIdInput.getUserId())).thenReturn(documentList);
        Mockito.when(mapperService.convertToUserValidationDocumentPayload(userValidationDocument)).thenReturn(userValidationDocumentPayload);

        List<UserValidationDocumentPayload> expectedResult = userValidationDocumentService.getAllUserValidationDocumentByUserId(getAllUserValidationDocumentByUserIdInput,httpServletRequest);

        assertEquals(expectedResult.size(),1);

        Mockito.verify(jwtTokenFilter).getJwtFromRequest(httpServletRequest);
        Mockito.verify(tokenManager).parseUserIdFromToken(token);
        Mockito.verify(userValidationDocumentRepository).findAllByUserId(getAllUserValidationDocumentByUserIdInput.getUserId());
        Mockito.verify(mapperService).convertToUserValidationDocumentPayload(userValidationDocument);

    }

    @DisplayName("should Return List UserDocumentPayload when UserId Is Exist In Token From Request And UserId From Token Not Equals UserId From GetAllUserValidationDocumentByUserIdInput")
    @Tag("getAllUserValidationDocumentByUserId")
    @Test
    void shouldReturnListUserDocumentPayload_whenUserIdIsExistInTokenFromRequestAndUserIdFromTokenNotEqualsToUserIdFromGetAllUserValidationDocumentByUserIdInput(){
        GetAllUserValidationDocumentByUserIdInput getAllUserValidationDocumentByUserIdInput = new GetAllUserValidationDocumentByUserIdInput("test_userId");
        String token = "Bearer test_Id";
        String userId = "test_Id";

        Mockito.when(jwtTokenFilter.getJwtFromRequest(httpServletRequest)).thenReturn(token);
        Mockito.when(tokenManager.parseUserIdFromToken(token)).thenReturn(userId);

        List<UserValidationDocumentPayload> expectedResult = userValidationDocumentService.getAllUserValidationDocumentByUserId(getAllUserValidationDocumentByUserIdInput,httpServletRequest);

        assertEquals(expectedResult.size(),0);

        Mockito.verify(jwtTokenFilter).getJwtFromRequest(httpServletRequest);
        Mockito.verify(tokenManager).parseUserIdFromToken(token);
    }

    @DisplayName("should Return UserValidationDocumentPayload when UserId Is Exist From Token And UserId From Token Equals UserId From AddUserValidationDocumentInput")
    @Tag("addUserValidationDocument")
    @Test
    void shouldReturnUserValidationDocumentPayload_whenUserIdIsExistFromTokenAndUserIdFromTokenEqualsUserIdFromAddUserValidationDocumentInput(){
        AddUserValidationDocumentInput addUserValidationDocumentInput = new AddUserValidationDocumentInput("test_userId","test_url",UserValidationDocumentType.TEST,new Date());
        String token = "Bearer test_userId";
        String userId = "test_userId";
        UserValidationDocument userValidationDocument = new UserValidationDocument(null,"test_userId","test_url", UserValidationDocumentType.TEST, UserValidationDocumentStatus.WAITING,false,new Date(),new Date());
        UserValidationDocument savedUserValidationDocument = new UserValidationDocument("test_userValidationDocumentId","test_userId","test_url", UserValidationDocumentType.TEST, UserValidationDocumentStatus.WAITING,false,userValidationDocument.getCreateDate(),userValidationDocument.getUpdateDate());
        UserValidationDocumentPayload userValidationDocumentPayload = new UserValidationDocumentPayload("test_userValidationDocumentId","test_url",UserValidationDocumentType.TEST,UserValidationDocumentStatus.WAITING,userValidationDocument.getCreateDate());


        Mockito.when(jwtTokenFilter.getJwtFromRequest(httpServletRequest)).thenReturn(token);
        Mockito.when(tokenManager.parseUserIdFromToken(token)).thenReturn(userId);
        Mockito.when(userValidationDocumentRepository.save(userValidationDocument)).thenReturn(savedUserValidationDocument);
        Mockito.when(mapperService.convertToUserValidationDocumentPayload(savedUserValidationDocument)).thenReturn(userValidationDocumentPayload);

        UserValidationDocumentPayload expectedResult = userValidationDocumentService.addUserValidationDocument(addUserValidationDocumentInput,httpServletRequest);

        assertNotNull(expectedResult);
        assertEquals(expectedResult,userValidationDocumentPayload);

        Mockito.verify(jwtTokenFilter).getJwtFromRequest(httpServletRequest);
        Mockito.verify(tokenManager).parseUserIdFromToken(token);
        Mockito.verify(userValidationDocumentRepository).save(userValidationDocument);
        Mockito.verify(mapperService).convertToUserValidationDocumentPayload(savedUserValidationDocument);

    }

    @DisplayName("should Return Null when UserId Is Exist From Token And UserId From Token Not Equals UserId From AddUserValidationDocumentInput")
    @Tag("addUserValidationDocument")
    @Test
    void shouldReturnNull_whenUserIdIsExistFromTokenAndUserIdFromTokenNotEqualsUserIdFromAddUserValidationDocumentInput(){
        AddUserValidationDocumentInput addUserValidationDocumentInput = new AddUserValidationDocumentInput("test_userId","test_url",UserValidationDocumentType.TEST,new Date());
        String token = "Bearer test_id";
        String userId = "test_id";
        Mockito.when(jwtTokenFilter.getJwtFromRequest(httpServletRequest)).thenReturn(token);
        Mockito.when(tokenManager.parseUserIdFromToken(token)).thenReturn(userId);

        UserValidationDocumentPayload expectedResult = userValidationDocumentService.addUserValidationDocument(addUserValidationDocumentInput,httpServletRequest);

        assertNull(expectedResult);

        Mockito.verify(jwtTokenFilter).getJwtFromRequest(httpServletRequest);
        Mockito.verify(tokenManager).parseUserIdFromToken(token);
    }

    @AfterEach
    void tearDown() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }
}
