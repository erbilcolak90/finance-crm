package com.financecrm.webportal;

import com.financecrm.webportal.entities.Role;
import com.financecrm.webportal.enums.SortBy;
import com.financecrm.webportal.input.PaginationInput;
import com.financecrm.webportal.input.role.CreateRoleInput;
import com.financecrm.webportal.input.role.DeleteRoleByNameInput;
import com.financecrm.webportal.input.role.GetAllRolesInput;
import com.financecrm.webportal.input.role.GetRoleIdByRoleNameInput;
import com.financecrm.webportal.payload.role.CreateRolePayload;
import com.financecrm.webportal.payload.role.DeleteRoleByNamePayload;
import com.financecrm.webportal.payload.role.GetRoleIdByRoleNamePayload;
import com.financecrm.webportal.repositories.RoleRepository;
import com.financecrm.webportal.services.RoleService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;
    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("should return string when roleId is exist and role isDeleted false")
    @Tag("findById")
    @Test
    void shouldReturnString_whenRoleIdIsExistAndRoleIsDeletedFalse() {
        String roleId = "test_id";
        Role role = new Role("test_id", "test_name", false);
        String result = role.getName();

        Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        String expectedResult = roleService.findById(roleId);

        assertEquals(expectedResult, result);

        Mockito.verify(roleRepository).findById(roleId);
    }

    @DisplayName("should return null when roleId is exist and role isDeletedTrue")
    @Tag("findById")
    @Test
    void shouldReturnNull_whenRoleIdIsExistAndRoleIsDeletedTrue() {
        String roleId = "test_id";
        Role role = new Role("test_id", "test_name", true);

        Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        String expectedResult = roleService.findById(roleId);

        assertNull(expectedResult);

        Mockito.verify(roleRepository).findById(roleId);
    }

    @DisplayName("should return null when role id does not exist")
    @Tag("findById")
    @Test
    void shouldReturnNull_whenRoleIdDoesNotExist() {
        String roleId = "test_id";

        Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        String expectedResult = roleService.findById(roleId);

        assertNull(expectedResult);

        Mockito.verify(roleRepository).findById(roleId);
    }

    @DisplayName("should return createrolepayload when roleName from createRoleInput does not exist")
    @Tag("createRole")
    @Test
    void shouldReturnCreateRolePayload_whenRoleNameFromCreateRoleInputDoesNotExist() {
        CreateRoleInput createRoleInput = new CreateRoleInput("test_name");
        Role role = new Role(null, "test_name", false);
        Role savedRole = new Role("test_id", "test_name", false);
        CreateRolePayload result = new CreateRolePayload(savedRole.getName() + " role created");

        Mockito.when(roleRepository.findByName(createRoleInput.getRoleName())).thenReturn(Optional.empty());
        Mockito.when(roleRepository.save(role)).thenReturn(savedRole);

        CreateRolePayload expectedResult = roleService.createRole(createRoleInput);

        assertEquals(expectedResult, result);

        Mockito.verify(roleRepository).findByName(createRoleInput.getRoleName());
        Mockito.verify(roleRepository).save(role);
    }

    @DisplayName("should return createrolepayload when roleName is exist and is deleted true")
    @Tag("createRole")
    @Test
    void shouldReturnCreateRolePayload_whenRoleNameIsExistAndIsDeletedTrue() {
        CreateRoleInput createRoleInput = new CreateRoleInput("test_name");
        Role role = new Role("test_id", "test_name", true);
        Role savedRole = new Role("test_id", "test_name", false);
        CreateRolePayload result = new CreateRolePayload(savedRole.getName() + " role can usable");

        Mockito.when(roleRepository.findByName(createRoleInput.getRoleName())).thenReturn(Optional.of(role));
        Mockito.when(roleRepository.save(role)).thenReturn(savedRole);

        CreateRolePayload expectedResult = roleService.createRole(createRoleInput);

        assertEquals(expectedResult, result);

        Mockito.verify(roleRepository).findByName(createRoleInput.getRoleName());
        Mockito.verify(roleRepository).save(role);
    }

    @DisplayName("should return null when roleName from createRoleInput is exist")
    @Tag("createRole")
    @Test
    void shouldReturnNull_whenRoleNameFromCreateRoleInputIsExist() {
        CreateRoleInput createRoleInput = new CreateRoleInput("test_name");
        Role role = new Role("test_id", "test_name", false);
        Mockito.when(roleRepository.findByName(createRoleInput.getRoleName())).thenReturn(Optional.of(role));
        CreateRolePayload expectedResult = roleService.createRole(createRoleInput);
        assertNull(expectedResult);
        Mockito.verify(roleRepository).findByName(createRoleInput.getRoleName());
    }

    @DisplayName("should return deleterolebynamepayload true when rolename from deleterolebynameinput is exist")
    @Tag("deleteRoleByName")
    @Test
    void shouldReturnDeleteRoleByNamePayloadTrue_whenRoleNameFromDeleteRoleByNameInputIsExist() {
        DeleteRoleByNameInput deleteRoleByNameInput = new DeleteRoleByNameInput("test_name");
        Role role = new Role("test_id", "test_name", false);

        Mockito.when(roleRepository.findByName(deleteRoleByNameInput.getRoleName())).thenReturn(Optional.of(role));
        Mockito.when(roleRepository.save(role)).thenReturn(null);

        DeleteRoleByNamePayload expectedResult = roleService.deleteRoleByName(deleteRoleByNameInput);

        assertTrue(expectedResult.isStatus());

        Mockito.verify(roleRepository).findByName(deleteRoleByNameInput.getRoleName());
        Mockito.verify(roleRepository).save(role);
    }

    @DisplayName("should return deleterolebynamepayload false when roleName from deleterolebynameinput does not exist")
    @Tag("deleteRoleByName")
    @Test
    void shouldReturnDeleteRoleByNamePayloadFalse_whenRoleNameFromDeleteRoleByNameInputDoesNotExist() {
        DeleteRoleByNameInput deleteRoleByNameInput = new DeleteRoleByNameInput("test_name");

        Mockito.when(roleRepository.findByName(deleteRoleByNameInput.getRoleName())).thenReturn(Optional.empty());
        DeleteRoleByNamePayload expectedResult = roleService.deleteRoleByName(deleteRoleByNameInput);

        assertFalse(expectedResult.isStatus());

        Mockito.verify(roleRepository).findByName(deleteRoleByNameInput.getRoleName());
    }

    @DisplayName("should return deleterolebynamepayload false when roleName from deleteRolebynameinput is exist and isDeleted true")
    @Tag("deleteRoleByName")
    @Test
    void shouldReturnDeleteRoleByNamePayloadFalse_whenRoleNameFromDeleteRoleByNameInputIsExistAndIsDeletedTrue() {
        DeleteRoleByNameInput deleteRoleByNameInput = new DeleteRoleByNameInput("test_name");
        Role role = new Role("test_id", "test_name", true);
        Mockito.when(roleRepository.findByName(deleteRoleByNameInput.getRoleName())).thenReturn(Optional.of(role));
        DeleteRoleByNamePayload expectedResult = roleService.deleteRoleByName(deleteRoleByNameInput);
        assertFalse(expectedResult.isStatus());

        Mockito.verify(roleRepository).findByName(deleteRoleByNameInput.getRoleName());
    }

    @DisplayName("should Return GetRoleIdByRoleNamePayload when RoleName From GetRoleIdByRoleNameInput Is Exist And IsDeleted False")
    @Tag("getRoleIdByRoleName")
    @Test
    void shouldReturnGetRoleIdByRoleNamePayload_whenRoleNameFromGetRoleIdByRoleNameInputIsExistAndIsDeletedFalse() {
        GetRoleIdByRoleNameInput getRoleIdByRoleNameInput = new GetRoleIdByRoleNameInput("test_name");
        Role role = new Role("test_id", "test_name", false);
        GetRoleIdByRoleNamePayload result = new GetRoleIdByRoleNamePayload(role.getId());

        Mockito.when(roleRepository.findByName(getRoleIdByRoleNameInput.getRoleName())).thenReturn(Optional.of(role));

        GetRoleIdByRoleNamePayload expectedResult = roleService.getRoleIdByRoleName(getRoleIdByRoleNameInput);

        assertEquals(expectedResult, result);

        Mockito.verify(roleRepository).findByName(getRoleIdByRoleNameInput.getRoleName());
    }

    @DisplayName("should Return Null when RoleName From GetRoleIdByRoleNameInput Is Exist And IsDeleted True")
    @Tag("getRoleIdByRoleName")
    @Test
    void shouldReturnNull_whenRoleNameFromGetRoleIdByRoleNameInputIsExistAndIsDeletedTrue() {
        GetRoleIdByRoleNameInput getRoleIdByRoleNameInput = new GetRoleIdByRoleNameInput("test_name");
        Role role = new Role("test_id", "test_name", true);

        Mockito.when(roleRepository.findByName(getRoleIdByRoleNameInput.getRoleName())).thenReturn(Optional.of(role));

        GetRoleIdByRoleNamePayload expectedResult = roleService.getRoleIdByRoleName(getRoleIdByRoleNameInput);

        assertNull(expectedResult);
        Mockito.verify(roleRepository).findByName(getRoleIdByRoleNameInput.getRoleName());
    }

    @DisplayName("should Return Null when RoleName From GetRoleIdByRoleNameInput Does Not Exist")
    @Tag("getRoleIdByRoleName")
    @Test
    void shouldReturnNull_whenRoleNameFromGetRoleIdByRoleNameInputDoesNotExist() {
        GetRoleIdByRoleNameInput getRoleIdByRoleNameInput = new GetRoleIdByRoleNameInput("test_name");

        Mockito.when(roleRepository.findByName(getRoleIdByRoleNameInput.getRoleName())).thenReturn(Optional.empty());

        GetRoleIdByRoleNamePayload expectedResult = roleService.getRoleIdByRoleName(getRoleIdByRoleNameInput);

        assertNull(expectedResult);

        Mockito.verify(roleRepository).findByName(getRoleIdByRoleNameInput.getRoleName());
    }

    @DisplayName("should return PageRole when GetAllRolesInput is exist")
    @Tag("getAllRoles")
    @Test
    void shouldReturnPageRole_whenGetAllRolesInputIsExist() {
        GetAllRolesInput getAllRolesInput = new GetAllRolesInput(new PaginationInput(0, 10, "name", SortBy.ASC));
        Role role = new Role("test_id", "test_name", false);
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        Page<Role> rolePage = new PageImpl<>(roleList);
        Pageable pageable = PageRequest.of(getAllRolesInput.getPaginationInput().getPage(), getAllRolesInput.getPaginationInput().getSize(), Sort.by(Sort.Direction.valueOf(getAllRolesInput.getPaginationInput().getSortBy().toString()), getAllRolesInput.getPaginationInput().getFieldName()));

        Mockito.when(roleRepository.findByIsDeletedFalse(pageable)).thenReturn(rolePage);

        Page<Role> expectedResult = roleService.getAllRoles(getAllRolesInput);

        assertEquals(1,expectedResult.getContent().size());

        Mockito.verify(roleRepository).findByIsDeletedFalse(pageable);
    }

    @AfterEach
    void tearDown() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

}
