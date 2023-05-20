package com.financecrm.webportal.admin;

import com.financecrm.webportal.entities.UserValidationDocument;
import com.financecrm.webportal.enums.UserValidationDocumentStatus;
import com.financecrm.webportal.enums.UserValidationDocumentType;
import com.financecrm.webportal.input.uservalidationdocument.UpdateUserValidationDocumentStatusInput;
import com.financecrm.webportal.repositories.UserValidationDocumentRepository;
import com.financecrm.webportal.services.admin.AdminUserValidationDocumentStatusService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AdminUserValidationDocumentStatusServiceTest {

    @InjectMocks
    private AdminUserValidationDocumentStatusService adminUserValidationDocumentStatusService;
    @Mock
    private UserValidationDocumentRepository userValidationDocumentRepository;


    @BeforeEach
    void setUp(){

        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("should Return UserValidationDocument when UserValidationDocumentId Is Exist And IsDeleted False In UserValidationDocuments From UpdateUserValidationDocumentStatusInput")
    @Tag("updateUserValidationDocumentStatus")
    @Test
    void shouldReturnUserValidationDocument_whenUserValidationDocumentIdIsExistAndIsDeletedFalseInUserValidationDocumentsFromUpdateUserValidationDocumentStatusInput(){
        UpdateUserValidationDocumentStatusInput updateUserValidationDocumentStatusInput = new UpdateUserValidationDocumentStatusInput("test_documentId", UserValidationDocumentStatus.APPROVED);
        UserValidationDocument savedUserValidationDocument = new UserValidationDocument("test_documentId","test_userId","test_url", UserValidationDocumentType.TEST,UserValidationDocumentStatus.APPROVED,false,new Date(),new Date());

        Mockito.when(userValidationDocumentRepository.findById(updateUserValidationDocumentStatusInput.getDocumentId())).thenReturn(Optional.of(new UserValidationDocument()));
        Mockito.when(userValidationDocumentRepository.save(ArgumentMatchers.any(UserValidationDocument.class))).thenReturn(savedUserValidationDocument);

        UserValidationDocument expectedResult = adminUserValidationDocumentStatusService.updateUserValidationDocumentStatus(updateUserValidationDocumentStatusInput);

        assertNotNull(expectedResult);
        assertEquals(UserValidationDocumentStatus.APPROVED, expectedResult.getStatus());

        Mockito.verify(userValidationDocumentRepository).findById(updateUserValidationDocumentStatusInput.getDocumentId());
        Mockito.verify(userValidationDocumentRepository).save(ArgumentMatchers.any(UserValidationDocument.class));
    }

    @DisplayName("should Return Null when UserValidationDocumentId Does Not Exist In UserValidationDocuments From UpdateUserValidationDocumentStatusInput")
    @Tag("updateUserValidationDocumentStatus")
    @Test
    void shouldReturnNull_whenUserValidationDocumentIdDoesNotExistInUserValidationDocumentsFromUpdateUserValidationDocumentStatusInput(){
        UpdateUserValidationDocumentStatusInput updateUserValidationDocumentStatusInput = new UpdateUserValidationDocumentStatusInput("test_documentId", UserValidationDocumentStatus.APPROVED);

        Mockito.when(userValidationDocumentRepository.findById(updateUserValidationDocumentStatusInput.getDocumentId())).thenReturn(Optional.empty());

        UserValidationDocument expectedResult = adminUserValidationDocumentStatusService.updateUserValidationDocumentStatus(updateUserValidationDocumentStatusInput);

        assertNull(expectedResult);

        Mockito.verify(userValidationDocumentRepository).findById(updateUserValidationDocumentStatusInput.getDocumentId());
    }

    @AfterEach
    void tearDown() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

}
