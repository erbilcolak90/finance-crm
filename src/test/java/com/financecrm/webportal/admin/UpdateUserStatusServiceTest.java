package com.financecrm.webportal.admin;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.enums.UserStatus;
import com.financecrm.webportal.input.user.UpdateUserStatusInput;
import com.financecrm.webportal.repositories.UserRepository;
import com.financecrm.webportal.services.admin.UpdateUserStatusService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UpdateUserStatusServiceTest {

    @InjectMocks
    private UpdateUserStatusService updateUserStatusService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("should Return User when User Id Is Exist And IsDeleted False In User From UpdateUserStatusInput")
    @Tag("updateUserStatus")
    @Test
    void shouldReturnUser_whenUserIdIsExistAndIsDeletedFalseInUserFromUpdateUserStatusInput(){
        UpdateUserStatusInput updateUserStatusInput = new UpdateUserStatusInput("test_userId",UserStatus.TEST);
        User user = new User();
        user.setId("test_id");
        user.setStatus(UserStatus.TEST);

        Mockito.when(userRepository.findById(updateUserStatusInput.getUserId())).thenReturn(Optional.of(new User()));
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        User expectedResult = updateUserStatusService.updateUserStatus(updateUserStatusInput);

        Assertions.assertNotNull(expectedResult);
        Assertions.assertEquals(expectedResult.getStatus(),updateUserStatusInput.getStatus());

        Mockito.verify(userRepository).findById(updateUserStatusInput.getUserId());
        Mockito.verify(userRepository).save(ArgumentMatchers.any(User.class));
    }

    @DisplayName("should Return Null when User Id Does Not Exist In User From UpdateUserStatusInput")
    @Tag("updateUserStatus")
    @Test
    void shouldReturnNull_whenUserIdDoesNotExistInUserFromUpdateUserStatusInput(){
        UpdateUserStatusInput updateUserStatusInput = new UpdateUserStatusInput("test_userId",UserStatus.TEST);

        Mockito.when(userRepository.findById(updateUserStatusInput.getUserId())).thenReturn(Optional.empty());

        User expectedResult = updateUserStatusService.updateUserStatus(updateUserStatusInput);

        Assertions.assertNull(expectedResult);

        Mockito.verify(userRepository).findById(updateUserStatusInput.getUserId());
    }

    @AfterEach
    void tearDown() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }
}
