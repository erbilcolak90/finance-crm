package com.financecrm.webportal;

import com.financecrm.webportal.entities.Role;
import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.entities.UserRole;
import com.financecrm.webportal.enums.UserStatus;
import com.financecrm.webportal.input.userrole.AddRoleToUserInput;
import com.financecrm.webportal.input.userrole.DeleteRoleFromUserInput;
import com.financecrm.webportal.input.userrole.GetUserRolesByUserIdInput;
import com.financecrm.webportal.payload.userrole.AddRoleToUserPayload;
import com.financecrm.webportal.payload.userrole.DeleteRoleFromUserPayload;
import com.financecrm.webportal.repositories.RoleRepository;
import com.financecrm.webportal.repositories.UserRoleRepository;
import com.financecrm.webportal.services.CustomUserService;
import com.financecrm.webportal.services.UserRoleService;
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
class UserRoleServiceTest {

    @InjectMocks
    private UserRoleService userRoleService;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private CustomUserService customUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("should Return List String when User Is Exist And IsDeleted False From GetUserRolesByUserIdInput")
    @Tag("getUserRolesByUserId")
    @Test
    void shouldReturnListString_whenUserIsExistAndIsDeletedFalseFromGetUserRolesByUserIdInput() {
        GetUserRolesByUserIdInput getUserRolesByUserIdInput = new GetUserRolesByUserIdInput("test_userId");
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        UserRole userRole = new UserRole("test_userRoleId", "test_roleId", "test_userId", false);
        Role role = new Role("test_roleId", "test_roleName", false);
        String roleId = "test_roleId";
        List<UserRole> roleList = new ArrayList<>();
        roleList.add(userRole);
        List<String> roleNameList = new ArrayList<>();
        roleNameList.add(roleId);


        Mockito.when(customUserService.findByUserId(getUserRolesByUserIdInput.getUserId())).thenReturn(user);
        Mockito.when(userRoleRepository.getUserRolesByUserId(getUserRolesByUserIdInput.getUserId())).thenReturn(roleList);
        Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));

        List<String> expectedResult = userRoleService.getUserRolesByUserId(getUserRolesByUserIdInput);

        assertEquals(expectedResult.size(), roleList.size());

        Mockito.verify(customUserService).findByUserId(getUserRolesByUserIdInput.getUserId());
        Mockito.verify(userRoleRepository).getUserRolesByUserId(getUserRolesByUserIdInput.getUserId());
        Mockito.verify(roleRepository).findById(role.getId());

    }

    @DisplayName("should Return List String when User Is Exist And IsDeleted True From GetUserRolesByUserIdInput")
    @Tag("getUserRolesByUserId")
    @Test
    void shouldReturnEmptyListString_whenUserIsExistAndIsDeletedTrueFromGetUserRolesByUserIdInput() {
        GetUserRolesByUserIdInput getUserRolesByUserIdInput = new GetUserRolesByUserIdInput("test_userId");
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, true, new Date(), new Date());

        Mockito.when(customUserService.findByUserId(getUserRolesByUserIdInput.getUserId())).thenReturn(user);

        List<String> expectedResult = userRoleService.getUserRolesByUserId(getUserRolesByUserIdInput);

        assertEquals(expectedResult.size(), 0);

        Mockito.verify(customUserService).findByUserId(getUserRolesByUserIdInput.getUserId());
    }

    @DisplayName("should Return List String when User Does Not Exist From GetUserRolesByUserIdInput")
    @Tag("getUserRolesByUserId")
    @Test
    void shouldReturnEmptyListString_whenUserDoesNotExistFromGetUserRolesByUserIdInput() {
        GetUserRolesByUserIdInput getUserRolesByUserIdInput = new GetUserRolesByUserIdInput("test_userId");
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, true, new Date(), new Date());

        Mockito.when(customUserService.findByUserId(getUserRolesByUserIdInput.getUserId())).thenReturn(null);

        List<String> expectedResult = userRoleService.getUserRolesByUserId(getUserRolesByUserIdInput);

        assertEquals(expectedResult.size(), 0);

        Mockito.verify(customUserService).findByUserId(getUserRolesByUserIdInput.getUserId());
    }

    @DisplayName("should Return AddRoleToUserPayload when UserId Is Exist In User And User IsDeleted False And RoleName Is Exist In Role And Role IsDeleted False And UserRolesList Does Not Any Match In UserRole From AddRoleToUserInput")
    @Tag("addRoleToUser")
    @Test
    void shouldReturnAddRoleToUserPayload_whenUserIdIsExistInUserAndUserIsDeletedFalseAndRoleNameIsExistInRoleAndRoleIsDeletedFalseAndUserRolesListDoesNotAnyMatchInUserRoleFromAddRoleToUserInput() {
        AddRoleToUserInput addRoleToUserInput = new AddRoleToUserInput("test_userId", "test_roleName");
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        Role role = new Role("test_roleId", "test_roleName", false);
        List<UserRole> roleList = new ArrayList<>();
        UserRole userRole = new UserRole(null, "test_roleId", "test_userId", false);
        UserRole savedUserRole = new UserRole("test_userRoleId", "test_roleId", "test_userId", false);
        AddRoleToUserPayload addRoleToUserPayload = new AddRoleToUserPayload(addRoleToUserInput.getRoleName() + " added to " + addRoleToUserInput.getUserId());

        Mockito.when(customUserService.findByUserId(addRoleToUserInput.getUserId())).thenReturn(user);
        Mockito.when(roleRepository.findByName(addRoleToUserInput.getRoleName())).thenReturn(Optional.of(role));
        Mockito.when(userRoleRepository.findByUserIdAndRoleId(addRoleToUserInput.getUserId(), role.getId())).thenReturn(roleList);
        Mockito.when(userRoleRepository.save(userRole)).thenReturn(savedUserRole);

        AddRoleToUserPayload expectedResult = userRoleService.addRoleToUser(addRoleToUserInput);

        assertEquals(expectedResult, addRoleToUserPayload);

        Mockito.verify(customUserService).findByUserId(addRoleToUserInput.getUserId());
        Mockito.verify(roleRepository).findByName(addRoleToUserInput.getRoleName());
        Mockito.verify(userRoleRepository).findByUserIdAndRoleId(addRoleToUserInput.getUserId(), role.getId());
        Mockito.verify(userRoleRepository).save(userRole);
    }

    @DisplayName("should Return AddRoleToUserPayload when UserId Is Exist In User And User IsDeleted False And RoleName Is Exist In Role And Role IsDeleted False And UserRolesList Any Match In UserRole From AddRoleToUserInput")
    @Tag("addRoleToUser")
    @Test
    void shouldReturnAddRoleToUserPayload_whenUserIdIsExistInUserAndUserIsDeletedFalseAndRoleNameIsExistInRoleAndRoleIsDeletedFalseAndUserRolesListAnyMatchInUserRoleFromAddRoleToUserInput() {
        AddRoleToUserInput addRoleToUserInput = new AddRoleToUserInput("test_userId", "test_roleName");
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        Role role = new Role("test_roleId", "test_roleName", false);
        List<UserRole> roleList = new ArrayList<>();
        UserRole userRole = new UserRole("test_userRoleId", "test_roleId", "test_userId", false);
        roleList.add(userRole);
        AddRoleToUserPayload addRoleToUserPayload = new AddRoleToUserPayload("User is already has this role");

        Mockito.when(customUserService.findByUserId(addRoleToUserInput.getUserId())).thenReturn(user);
        Mockito.when(roleRepository.findByName(addRoleToUserInput.getRoleName())).thenReturn(Optional.of(role));
        Mockito.when(userRoleRepository.findByUserIdAndRoleId(addRoleToUserInput.getUserId(), role.getId())).thenReturn(roleList);

        AddRoleToUserPayload expectedResult = userRoleService.addRoleToUser(addRoleToUserInput);

        assertEquals(expectedResult, addRoleToUserPayload);

        Mockito.verify(customUserService).findByUserId(addRoleToUserInput.getUserId());
        Mockito.verify(roleRepository).findByName(addRoleToUserInput.getRoleName());
        Mockito.verify(userRoleRepository).findByUserIdAndRoleId(addRoleToUserInput.getUserId(), role.getId());

    }

    @DisplayName("should Return AddRoleToUserPayload when UserId Is Exist In User And User IsDeleted False And RoleName Is Exist In Role And Role IsDeleted True From AddRoleToUserInput")
    @Tag("addRoleToUser")
    @Test
    void shouldReturnAddRoleToUserPayload_whenUserIdIsExistInUserAndUserIsDeletedFalseAndRoleNameIsExistInRoleAndRoleIsDeletedTrueFromAddRoleToUserInput() {
        AddRoleToUserInput addRoleToUserInput = new AddRoleToUserInput("test_userId", "test_roleName");
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        Role role = new Role("test_roleId", "test_roleName", true);
        List<UserRole> roleList = new ArrayList<>();
        UserRole userRole = new UserRole("test_userRoleId", "test_roleId", "test_userId", false);
        roleList.add(userRole);
        AddRoleToUserPayload addRoleToUserPayload = new AddRoleToUserPayload("User or rolename not found");

        Mockito.when(customUserService.findByUserId(addRoleToUserInput.getUserId())).thenReturn(user);
        Mockito.when(roleRepository.findByName(addRoleToUserInput.getRoleName())).thenReturn(Optional.of(role));

        AddRoleToUserPayload expectedResult = userRoleService.addRoleToUser(addRoleToUserInput);

        assertEquals(expectedResult, addRoleToUserPayload);

        Mockito.verify(customUserService).findByUserId(addRoleToUserInput.getUserId());
        Mockito.verify(roleRepository).findByName(addRoleToUserInput.getRoleName());
    }

    @DisplayName("should Return AddRoleToUserPayload when UserId Is Exist In User And User IsDeleted False And RoleName Does Not Exist From AddRoleToUserInput")
    @Tag("addRoleToUser")
    @Test
    void shouldReturnAddRoleToUserPayload_whenUserIdIsExistInUserAndUserIsDeletedFalseAndRoleNameDoesNotExistInRoleFromAddRoleToUserInput() {
        AddRoleToUserInput addRoleToUserInput = new AddRoleToUserInput("test_userId", "test_roleName");
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        Role role = new Role("test_roleId", "test_roleName", true);
        List<UserRole> roleList = new ArrayList<>();
        UserRole userRole = new UserRole("test_userRoleId", "test_roleId", "test_userId", false);
        roleList.add(userRole);
        AddRoleToUserPayload addRoleToUserPayload = new AddRoleToUserPayload("User or rolename not found");

        Mockito.when(customUserService.findByUserId(addRoleToUserInput.getUserId())).thenReturn(user);
        Mockito.when(roleRepository.findByName(addRoleToUserInput.getRoleName())).thenReturn(Optional.empty());

        AddRoleToUserPayload expectedResult = userRoleService.addRoleToUser(addRoleToUserInput);

        assertEquals(expectedResult, addRoleToUserPayload);

        Mockito.verify(customUserService).findByUserId(addRoleToUserInput.getUserId());
        Mockito.verify(roleRepository).findByName(addRoleToUserInput.getRoleName());
    }

    @DisplayName("should Return AddRoleToUserPayload when UserId Is Exist In User And User IsDeleted True From AddRoleToUserInput")
    @Tag("addRoleToUser")
    @Test
    void shouldReturnAddRoleToUserPayload_whenUserIdIsExistInUserAndUserIsDeletedTrueFromAddRoleToUserInput() {
        AddRoleToUserInput addRoleToUserInput = new AddRoleToUserInput("test_userId", "test_roleName");
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, true, new Date(), new Date());
        Role role = new Role("test_roleId", "test_roleName", true);

        AddRoleToUserPayload addRoleToUserPayload = new AddRoleToUserPayload("User or rolename not found");

        Mockito.when(customUserService.findByUserId(addRoleToUserInput.getUserId())).thenReturn(user);
        Mockito.when(roleRepository.findByName(addRoleToUserInput.getRoleName())).thenReturn(Optional.empty());

        AddRoleToUserPayload expectedResult = userRoleService.addRoleToUser(addRoleToUserInput);

        assertEquals(expectedResult, addRoleToUserPayload);

        Mockito.verify(customUserService).findByUserId(addRoleToUserInput.getUserId());
        Mockito.verify(roleRepository).findByName(addRoleToUserInput.getRoleName());
    }

    @DisplayName("should Return AddRoleToUserPayload when UserId Does Not Exist In User From AddRoleToUserInput")
    @Tag("addRoleToUser")
    @Test
    void shouldReturnAddRoleToUserPayload_whenUserIdDoesNotExistInUserFromAddRoleToUserInput() {
        AddRoleToUserInput addRoleToUserInput = new AddRoleToUserInput("test_userId", "test_roleName");

        AddRoleToUserPayload addRoleToUserPayload = new AddRoleToUserPayload("User or rolename not found");

        Mockito.when(customUserService.findByUserId(addRoleToUserInput.getUserId())).thenReturn(null);
        Mockito.when(roleRepository.findByName(addRoleToUserInput.getRoleName())).thenReturn(Optional.empty());

        AddRoleToUserPayload expectedResult = userRoleService.addRoleToUser(addRoleToUserInput);

        assertEquals(expectedResult, addRoleToUserPayload);

        Mockito.verify(customUserService).findByUserId(addRoleToUserInput.getUserId());
        Mockito.verify(roleRepository).findByName(addRoleToUserInput.getRoleName());
    }

    @DisplayName("should Return DeleteRoleFromUserPayload Status True when UserId Is Exist In User And Role Is Exist In Role And User IsDeleted False And Role IsDeleted False And UserRoleName Is Exist In UserRoles And UserRoleSize Not Equals userRoleSize From DeleteRoleFromUserInput")
    @Tag("deleteRoleFromUser")
    @Test
    void shouldReturnDeleteRoleFromUserPayloadStatusTrue_whenUserIdIsExistInUserAndRoleIsExistInRoleAndUserIsDeletedFalseAndRoleIsDeletedFalseAndUserRoleNameIsExistInUserRolesFromDeleteRoleFromUserInput() {
        DeleteRoleFromUserInput deleteRoleFromUserInput = new DeleteRoleFromUserInput("test_userId", "test_roleName");
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        Role role = new Role("test_roleId", "test_roleName", false);
        List<UserRole> roleList = new ArrayList<>();
        UserRole userRole = new UserRole("test_userRoleId", "test_roleId", "test_userId", false);
        roleList.add(userRole);

        Mockito.when(customUserService.findByUserId(deleteRoleFromUserInput.getUserId())).thenReturn(user);
        Mockito.when(roleRepository.findByName(deleteRoleFromUserInput.getRoleName())).thenReturn(Optional.of(role));
        Mockito.when(userRoleRepository.findByUserIdAndRoleId(deleteRoleFromUserInput.getUserId(), deleteRoleFromUserInput.getRoleName())).thenReturn(roleList);
        Mockito.when(userRoleRepository.save(userRole)).thenReturn(null);

        DeleteRoleFromUserPayload expectedResult = userRoleService.deleteRoleFromUser(deleteRoleFromUserInput);

        assertTrue(expectedResult.isStatus());

        Mockito.verify(customUserService).findByUserId(deleteRoleFromUserInput.getUserId());
        Mockito.verify(roleRepository).findByName(deleteRoleFromUserInput.getRoleName());
        Mockito.verify(userRoleRepository).findByUserIdAndRoleId(deleteRoleFromUserInput.getUserId(), deleteRoleFromUserInput.getRoleName());
        Mockito.verify(userRoleRepository).save(userRole);

    }

    @DisplayName("should Return DeleteRoleFromUserPayload Status False when UserId Is Exist In User And Role Is Exist In Role And User IsDeleted False And Role IsDeleted True From DeleteRoleFromUserInput")
    @Tag("deleteRoleFromUser")
    @Test
    void shouldReturnDeleteRoleFromUserPayloadStatusFalse_whenUserIdIsExistInUserAndRoleIsExistInRoleAndUserIsDeletedFalseAndRoleIsDeletedTrueFromDeleteRoleFromUserInput() {
        DeleteRoleFromUserInput deleteRoleFromUserInput = new DeleteRoleFromUserInput("test_userId", "test_roleName");
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        Role role = new Role("test_roleId", "test_roleName", true);

        Mockito.when(customUserService.findByUserId(deleteRoleFromUserInput.getUserId())).thenReturn(user);
        Mockito.when(roleRepository.findByName(deleteRoleFromUserInput.getRoleName())).thenReturn(Optional.of(role));

        DeleteRoleFromUserPayload expectedResult = userRoleService.deleteRoleFromUser(deleteRoleFromUserInput);

        assertFalse(expectedResult.isStatus());

        Mockito.verify(customUserService).findByUserId(deleteRoleFromUserInput.getUserId());
        Mockito.verify(roleRepository).findByName(deleteRoleFromUserInput.getRoleName());

    }

    @DisplayName("should Return DeleteRoleFromUserPayload Status False when UserId Is Exist In User And Role Is Exist In Role And User IsDeleted True From DeleteRoleFromUserInput")
    @Tag("deleteRoleFromUser")
    @Test
    void shouldReturnDeleteRoleFromUserPayloadStatusFalse_whenUserIdIsExistInUserAndRoleIsExistInRoleAndUserIsDeletedTrueFromDeleteRoleFromUserInput() {
        DeleteRoleFromUserInput deleteRoleFromUserInput = new DeleteRoleFromUserInput("test_userId", "test_roleName");
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, true, new Date(), new Date());
        Role role = new Role("test_roleId", "test_roleName", true);

        Mockito.when(customUserService.findByUserId(deleteRoleFromUserInput.getUserId())).thenReturn(user);
        Mockito.when(roleRepository.findByName(deleteRoleFromUserInput.getRoleName())).thenReturn(Optional.of(role));

        DeleteRoleFromUserPayload expectedResult = userRoleService.deleteRoleFromUser(deleteRoleFromUserInput);

        assertFalse(expectedResult.isStatus());

        Mockito.verify(customUserService).findByUserId(deleteRoleFromUserInput.getUserId());
        Mockito.verify(roleRepository).findByName(deleteRoleFromUserInput.getRoleName());

    }

    @DisplayName("should Return DeleteRoleFromUserPayload Status False when UserId Is Exist In User And Role Does Not Exist In Role From DeleteRoleFromUserInput")
    @Tag("deleteRoleFromUser")
    @Test
    void shouldReturnDeleteRoleFromUserPayloadStatusFalse_whenUserIdIsExistInUserAndRoleDoesNotExistInRoleFromUserInput() {
        DeleteRoleFromUserInput deleteRoleFromUserInput = new DeleteRoleFromUserInput("test_userId", "test_roleName");
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());

        Mockito.when(customUserService.findByUserId(deleteRoleFromUserInput.getUserId())).thenReturn(user);
        Mockito.when(roleRepository.findByName(deleteRoleFromUserInput.getRoleName())).thenReturn(Optional.empty());

        DeleteRoleFromUserPayload expectedResult = userRoleService.deleteRoleFromUser(deleteRoleFromUserInput);

        assertFalse(expectedResult.isStatus());

        Mockito.verify(customUserService).findByUserId(deleteRoleFromUserInput.getUserId());
        Mockito.verify(roleRepository).findByName(deleteRoleFromUserInput.getRoleName());

    }

    @DisplayName("should Return DeleteRoleFromUserPayload Status False when UserId Does Not Exist In User From DeleteRoleFromUserInput")
    @Tag("deleteRoleFromUser")
    @Test
    void shouldReturnDeleteRoleFromUserPayloadStatusFalse_whenUserIdDoesNotExistInUserFromDeleteRoleFromUserInput() {
        DeleteRoleFromUserInput deleteRoleFromUserInput = new DeleteRoleFromUserInput("test_userId", "test_roleName");

        Mockito.when(customUserService.findByUserId(deleteRoleFromUserInput.getUserId())).thenReturn(null);
        Mockito.when(roleRepository.findByName(deleteRoleFromUserInput.getRoleName())).thenReturn(Optional.empty());

        DeleteRoleFromUserPayload expectedResult = userRoleService.deleteRoleFromUser(deleteRoleFromUserInput);

        assertFalse(expectedResult.isStatus());

        Mockito.verify(customUserService).findByUserId(deleteRoleFromUserInput.getUserId());
        Mockito.verify(roleRepository).findByName(deleteRoleFromUserInput.getRoleName());
    }

    @AfterEach
    void tearDown() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }
}
