package com.financecrm.webportal;

import com.financecrm.webportal.entities.Department;
import com.financecrm.webportal.enums.SortBy;
import com.financecrm.webportal.input.PaginationInput;
import com.financecrm.webportal.input.department.CreateDepartmentInput;
import com.financecrm.webportal.input.department.DeleteDepartmentInput;
import com.financecrm.webportal.input.department.GetAllDepartmentsInput;
import com.financecrm.webportal.input.department.GetDepartmentByIdInput;
import com.financecrm.webportal.payload.department.CreateDepartmentPayload;
import com.financecrm.webportal.payload.department.DeleteDepartmentPayload;
import com.financecrm.webportal.payload.department.DepartmentPayload;
import com.financecrm.webportal.repositories.DepartmentRepository;
import com.financecrm.webportal.services.CustomUserService;
import com.financecrm.webportal.services.DepartmentService;
import com.financecrm.webportal.services.MapperService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @InjectMocks
    private DepartmentService departmentService;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private CustomUserService customUserService;
    @Mock
    private MapperService mapperService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("should return createdepartmentpayload when departmentid from createDepartmentinput is exist")
    @Tag("createDepartment")
    @Test
    void shouldReturnCreateDepartmentPayload_whenDepartmentIdFromCreateDepartmentInputIsExist(){
        CreateDepartmentInput createDepartmentInput = new CreateDepartmentInput("test_name","test_managerId");
        Department department = new Department(null,"test_name","test_managerId",false);
        Department savedDepartment = new Department("test_id","test_name","test_managerId",false);
        CreateDepartmentPayload result = new CreateDepartmentPayload("test_id","test_name","test_managerId");

        Mockito.when(departmentRepository.findByName(createDepartmentInput.getName())).thenReturn(Optional.empty());
        Mockito.when(departmentRepository.save(department)).thenReturn(savedDepartment);
        Mockito.when(mapperService.convertToCreateDepartmentPayload(savedDepartment)).thenReturn(result);

        CreateDepartmentPayload expectedResult = departmentService.createDepartment(createDepartmentInput);

        assertEquals(expectedResult,result);

        Mockito.verify(departmentRepository).findByName(createDepartmentInput.getName());
        Mockito.verify(departmentRepository).save(department);
        Mockito.verify(mapperService).convertToCreateDepartmentPayload(savedDepartment);
    }

    @DisplayName("should return null when department id from createdepartmentinput does not exist")
    @Tag("createDepartment")
    @Test
    void shouldReturnNull_whenDepartmentIdFromCreateDepartmentInputDoesNotExist(){
        CreateDepartmentInput createDepartmentInput = new CreateDepartmentInput("test_name","test_managerId");
        Department department = new Department("test_id","test_name","test_managerId",false);

        Mockito.when(departmentRepository.findByName(createDepartmentInput.getName())).thenReturn(Optional.of(department));

        CreateDepartmentPayload expectedResult = departmentService.createDepartment(createDepartmentInput);

        assertNull(expectedResult);

        Mockito.verify(departmentRepository).findByName(createDepartmentInput.getName());

    }

    @DisplayName("should return departmentpayload when departmentid from getdepartmentbyidinput is exist")
    @Tag("getDepartmentById")
    @Test
    void shouldReturnDepartmentPayload_whenDepartmentIdFromGetDepartmentByIdInputIsExist(){
        GetDepartmentByIdInput getDepartmentByIdInput = new GetDepartmentByIdInput("test_id");
        Department department = new Department("test_id","test_name","test_managerId",false);
        DepartmentPayload departmentPayload = new DepartmentPayload("test_id","test_name","test_managerId");

        Mockito.when(departmentRepository.findById(getDepartmentByIdInput.getId())).thenReturn(Optional.of(department));
        Mockito.when(mapperService.convertToGetDepartmentById(department)).thenReturn(departmentPayload);

        DepartmentPayload expectedResult = departmentService.getDepartmentById(getDepartmentByIdInput);

        assertEquals(expectedResult,departmentPayload);

        Mockito.verify(departmentRepository).findById(getDepartmentByIdInput.getId());
        Mockito.verify(mapperService).convertToGetDepartmentById(department);
    }

    @DisplayName("should return null when departmentid from getdepartmentbyidinput does not exist")
    @Tag("getDepartmentById")
    @Test
    void shouldReturnNull_whenDepartmentIdFromGetDepartmentByIdInputDoesNotExist(){
        GetDepartmentByIdInput getDepartmentByIdInput = new GetDepartmentByIdInput("test_id");
        Department department = null;
        DepartmentPayload departmentPayload = null;

        Mockito.when(departmentRepository.findById(getDepartmentByIdInput.getId())).thenReturn(Optional.empty());
        Mockito.when(mapperService.convertToGetDepartmentById(department)).thenReturn(departmentPayload);

        DepartmentPayload expectedResult = departmentService.getDepartmentById(getDepartmentByIdInput);

        assertNull(expectedResult);

        Mockito.verify(departmentRepository).findById(getDepartmentByIdInput.getId());
        Mockito.verify(mapperService).convertToGetDepartmentById(department);
    }

    @DisplayName("should return page departmentpayload when getalldepartmentsinput is exist")
    @Tag("getAllDepartments")
    @Test
    void shouldReturnPageDepartmentPayload_whenGetAllDepartmentsInputIsExist(){
        GetAllDepartmentsInput getAllDepartmentsInput = new GetAllDepartmentsInput(new PaginationInput(0,10,"id", SortBy.ASC));
        Department department = new Department("test_id","test_name","test_managerId",false);
        DepartmentPayload departmentPayload = new DepartmentPayload("test_id","test_name","test_managerId");
        List<Department> departmentList = new ArrayList<>();
        departmentList.add(department);
        Page<Department> departmentPage = new PageImpl<>(departmentList);

        Pageable pageable = PageRequest.of(getAllDepartmentsInput.getPagination().getPage(),
                getAllDepartmentsInput.getPagination().getSize(),
                Sort.by(Sort.Direction.valueOf(getAllDepartmentsInput.getPagination().getSortBy().toString()),
                        getAllDepartmentsInput.getPagination().getFieldName()));

        Mockito.when(departmentRepository.findByIsDeletedFalse(pageable)).thenReturn(departmentPage);
        Mockito.when(mapperService.convertToDepartmentPayload(department)).thenReturn(departmentPayload);

        Page<DepartmentPayload> expectedResult = departmentService.getAllDepartments(getAllDepartmentsInput);

        assertEquals(1,expectedResult.getContent().size());

        Mockito.verify(departmentRepository).findByIsDeletedFalse(pageable);
        Mockito.verify(mapperService).convertToDepartmentPayload(department);
    }

    @DisplayName("should return deletedepartmentpayload true when id from deletedepartmentinput is exist")
    @Tag("deleteDepartment")
    @Test
    void shouldReturnDeleteDepartmentPayloadTrue_whenIdFromDeleteDepartmentInputIsExist(){
        DeleteDepartmentInput deleteDepartmentInput = new DeleteDepartmentInput("test_id");
        Department department = new Department("test_id","test_name","test_managerId",false);

        Mockito.when(departmentRepository.findById(deleteDepartmentInput.getId())).thenReturn(Optional.of(department));
        Mockito.when(departmentRepository.save(department)).thenReturn(null);

        DeleteDepartmentPayload expectedResult = departmentService.deleteDepartment(deleteDepartmentInput);

        assertTrue(expectedResult.isStatus());

        Mockito.verify(departmentRepository).findById(deleteDepartmentInput.getId());
        Mockito.verify(departmentRepository).save(department);
    }

    @DisplayName("should return deletedepartmentpayload false when id from deletedepartmentinput does not exist")
    @Tag("deleteDepartment")
    @Test
    void shouldReturnDeleteDepartmentPayloadFalse_whenIdFromDeleteDepartmentInputDoesNotExist(){
        DeleteDepartmentInput deleteDepartmentInput = new DeleteDepartmentInput("test_id");
        Department department = null;

        Mockito.when(departmentRepository.findById(deleteDepartmentInput.getId())).thenReturn(Optional.empty());

        DeleteDepartmentPayload expectedResult = departmentService.deleteDepartment(deleteDepartmentInput);

        assertFalse(expectedResult.isStatus());

        Mockito.verify(departmentRepository).findById(deleteDepartmentInput.getId());
    }

    @DisplayName("should return deletedepartmentpayload false when id from deletedepartmentinput does not exist")
    @Tag("deleteDepartment")
    @Test
    void shouldReturnDeleteDepartmentPayloadFalse_whenIdFromDeleteDepartmentInputIsDeletedTrue(){
        DeleteDepartmentInput deleteDepartmentInput = new DeleteDepartmentInput("test_id");
        Department department = new Department("test_id","test_name","test_managerId",true);

        Mockito.when(departmentRepository.findById(deleteDepartmentInput.getId())).thenReturn(Optional.of(department));

        DeleteDepartmentPayload expectedResult = departmentService.deleteDepartment(deleteDepartmentInput);

        assertFalse(expectedResult.isStatus());

        Mockito.verify(departmentRepository).findById(deleteDepartmentInput.getId());
    }

    @AfterEach
    void tearDown() {
    }

}
