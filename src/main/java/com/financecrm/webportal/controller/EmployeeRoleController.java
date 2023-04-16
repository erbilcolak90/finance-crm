package com.financecrm.webportal.controller;

import com.financecrm.webportal.input.employeerole.AddRoleToEmployeeInput;
import com.financecrm.webportal.input.employeerole.DeleteRoleNameFromEmployeeInput;
import com.financecrm.webportal.payload.employeerole.AddRoleToEmployeePayload;
import com.financecrm.webportal.payload.employeerole.DeleteRoleNameFromEmployeePayload;
import com.financecrm.webportal.services.EmployeeRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employeeRole")
@RequiredArgsConstructor
@CrossOrigin
public class EmployeeRoleController {

    @Autowired
    private EmployeeRoleService employeeRoleService;

    @PostMapping("/addRoleToEmployee")
    public ResponseEntity<AddRoleToEmployeePayload> addRoleToEmployee(@RequestBody AddRoleToEmployeeInput addRoleToEmployeeInput){
        return ResponseEntity.ok(employeeRoleService.addRoleToEmployee(addRoleToEmployeeInput));
    }

    @PostMapping("/deleteRoleNameFromEmployee")
    public ResponseEntity<DeleteRoleNameFromEmployeePayload> deleteRoleNameFromEmployee(@RequestBody DeleteRoleNameFromEmployeeInput deleteRoleNameFromEmployeeInput){
        return ResponseEntity.ok(employeeRoleService.deleteRoleNameFromEmployee(deleteRoleNameFromEmployeeInput));
    }


}
