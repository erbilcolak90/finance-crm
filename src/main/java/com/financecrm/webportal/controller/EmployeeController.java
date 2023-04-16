package com.financecrm.webportal.controller;

import com.financecrm.webportal.input.employee.*;
import com.financecrm.webportal.payload.employee.EmployeePayload;
import com.financecrm.webportal.payload.employee.CreateEmployeePayload;
import com.financecrm.webportal.payload.employee.DeleteEmployeePayload;
import com.financecrm.webportal.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@CrossOrigin
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/createEmployee")
    public ResponseEntity<CreateEmployeePayload> createEmployee(@RequestBody CreateEmployeeInput createEmployeeInput){
        return ResponseEntity.ok(employeeService.createEmployee(createEmployeeInput));
    }

    @PostMapping("/deleteEmployee")
    public ResponseEntity<DeleteEmployeePayload> deleteEmployee(@RequestBody DeleteEmployeeInput deleteEmployeeInput){
        return ResponseEntity.ok(employeeService.deleteEmployee(deleteEmployeeInput));
    }

    public ResponseEntity<EmployeePayload> updateEmployee(@RequestBody UpdateEmployeeInput updateEmployeeInput){
        return ResponseEntity.ok(employeeService.updateEmployeeInput(updateEmployeeInput));
    }

    @PostMapping("/getEmployeeById")
    public ResponseEntity<EmployeePayload> getEmployeeById(@RequestBody GetEmployeeByIdInput getEmployeeByIdInput){
        return ResponseEntity.ok(employeeService.getEmployeeById(getEmployeeByIdInput));
    }

    @PostMapping("/getAllEmployee")
    public Page<EmployeePayload> getAllEmployees(@RequestBody GetAllEmployeesInput getAllEmployeesInput){
        return employeeService.getAllEmployees(getAllEmployeesInput);
    }

}
