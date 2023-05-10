package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.Department;
import com.financecrm.webportal.input.department.CreateDepartmentInput;
import com.financecrm.webportal.input.department.DeleteDepartmentInput;
import com.financecrm.webportal.input.department.GetAllDepartmentsInput;
import com.financecrm.webportal.input.department.GetDepartmentByIdInput;
import com.financecrm.webportal.payload.department.CreateDepartmentPayload;
import com.financecrm.webportal.payload.department.DeleteDepartmentPayload;
import com.financecrm.webportal.payload.department.DepartmentPayload;
import com.financecrm.webportal.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CustomUserService customUserService;
    @Autowired
    private MapperService mapperService;

    @Transactional
    public CreateDepartmentPayload createDepartment(CreateDepartmentInput createDepartmentInput) {

        Department db_department = departmentRepository.findByName(createDepartmentInput.getName().toLowerCase()).orElse(null);
        if (db_department == null) {
            Department department = new Department();
            department.setName(createDepartmentInput.getName().toLowerCase());
            department.setManagerId(createDepartmentInput.getManagerId());
            department.setDeleted(false);
            Department savedDepartment = departmentRepository.save(department);
            log.info("department created");
            return mapperService.convertToCreateDepartmentPayload(savedDepartment);
        } else {
            return null;
        }
    }

    public DepartmentPayload getDepartmentById(GetDepartmentByIdInput getDepartmentByIdInput) {

        Department department = departmentRepository.findById(getDepartmentByIdInput.getId()).orElse(null);
        return mapperService.convertToGetDepartmentById(department);
    }

    public Page<DepartmentPayload> getAllDepartments(GetAllDepartmentsInput getAllDepartmentsInput) {

        Pageable pageable = PageRequest.of(getAllDepartmentsInput.getPagination().getPage(),
                getAllDepartmentsInput.getPagination().getSize(),
                Sort.by(Sort.Direction.valueOf(getAllDepartmentsInput.getPagination().getSortBy().toString()),
                        getAllDepartmentsInput.getPagination().getFieldName()));

        Page<Department> departmentsPage = departmentRepository.findByIsDeletedFalse(pageable);

        return departmentsPage.map(department -> mapperService.convertToDepartmentPayload(department));
    }

    public DeleteDepartmentPayload deleteDepartment(DeleteDepartmentInput deleteDepartmentInput) {

        Department db_department = departmentRepository.findById(deleteDepartmentInput.getId()).orElse(null);

        if (db_department != null && !db_department.isDeleted()) {
            db_department.setDeleted(true);
            departmentRepository.save(db_department);
            return new DeleteDepartmentPayload(true);
        } else {
            log.info("db_department null or already deleted");
            return new DeleteDepartmentPayload(false);
        }

    }
}
