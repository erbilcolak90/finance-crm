package com.financecrm.webportal.controller;

import com.financecrm.webportal.input.department.CreateDepartmentInput;
import com.financecrm.webportal.input.department.DeleteDepartmentInput;
import com.financecrm.webportal.input.department.GetAllDepartmentsInput;
import com.financecrm.webportal.input.department.GetDepartmentByIdInput;
import com.financecrm.webportal.payload.department.CreateDepartmentPayload;
import com.financecrm.webportal.payload.department.DeleteDepartmentPayload;
import com.financecrm.webportal.payload.department.DepartmentPayload;
import com.financecrm.webportal.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
@CrossOrigin
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/createDepartment")
    public ResponseEntity<CreateDepartmentPayload> createDepartment(@RequestBody CreateDepartmentInput createDepartmentInput) {
        return ResponseEntity.ok(departmentService.createDepartment(createDepartmentInput));
    }

    @PostMapping("/getDepartmentById")
    public ResponseEntity<DepartmentPayload> getDepartmentById(@RequestBody GetDepartmentByIdInput getDepartmentByIdInput){
        return ResponseEntity.ok(departmentService.getDepartmentById(getDepartmentByIdInput));
    }

    @PostMapping("/getAllDepartments")
    public Page<DepartmentPayload> getAllDepartments(@RequestBody GetAllDepartmentsInput getAllDepartmentsInput){
        return departmentService.getAllDepartments(getAllDepartmentsInput);
    }

    @PostMapping("/deleteDepartment")
    public ResponseEntity<DeleteDepartmentPayload> deleteDepartment(@RequestBody DeleteDepartmentInput deleteDepartmentInput){
        return ResponseEntity.ok(departmentService.deleteDepartment(deleteDepartmentInput));
    }
}
