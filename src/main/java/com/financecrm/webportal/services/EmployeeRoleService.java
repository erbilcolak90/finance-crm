package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.Employee;
import com.financecrm.webportal.entities.EmployeeRole;
import com.financecrm.webportal.input.employeerole.AddRoleToEmployeeInput;
import com.financecrm.webportal.input.employeerole.DeleteRoleNameFromEmployeeInput;
import com.financecrm.webportal.input.role.GetRoleIdByRoleNameInput;
import com.financecrm.webportal.payload.employeerole.AddRoleToEmployeePayload;
import com.financecrm.webportal.payload.employeerole.DeleteRoleNameFromEmployeePayload;
import com.financecrm.webportal.payload.role.GetRoleIdByRoleNamePayload;
import com.financecrm.webportal.repositories.EmployeeRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeRoleService {

    @Autowired
    private EmployeeRoleRepository employeeRoleRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MapperService mapperService;

    @Transactional
    public AddRoleToEmployeePayload addRoleToEmployee(AddRoleToEmployeeInput addRoleToEmployeeInput) {

        Employee db_employee= employeeService.findById(addRoleToEmployeeInput.getEmployeeId());
        GetRoleIdByRoleNameInput getRoleIdByRoleNameInput = new GetRoleIdByRoleNameInput(addRoleToEmployeeInput.getRoleName());
        GetRoleIdByRoleNamePayload db_role = roleService.getRoleIdByRoleName(getRoleIdByRoleNameInput);
        EmployeeRole db_employeeRole = employeeRoleRepository.findByEmployeeIdAndRoleId(db_employee.getId(),db_role.getRoleId());

        if(db_employee != null &&
                db_role != null &&
                db_employeeRole == null &&
                !db_employee.isDeleted()){
            EmployeeRole employeeRole = new EmployeeRole();
            employeeRole.setRoleId(db_role.getRoleId());
            employeeRole.setEmployeeId(db_employee.getId());
            employeeRole.setDeleted(false);
            employeeRoleRepository.save(employeeRole);
            log.info(addRoleToEmployeeInput.getRoleName() + " added to employeeId: " + db_employee.getId());

            return new AddRoleToEmployeePayload(true);
        }else {
            return new AddRoleToEmployeePayload(false);
        }
    }

    @Transactional
    public DeleteRoleNameFromEmployeePayload deleteRoleNameFromEmployee(DeleteRoleNameFromEmployeeInput deleteRoleNameFromEmployeeInput) {

        Employee db_employee = employeeService.findById(deleteRoleNameFromEmployeeInput.getEmployeeId());
        GetRoleIdByRoleNameInput getRoleIdByRoleNameInput = new GetRoleIdByRoleNameInput(deleteRoleNameFromEmployeeInput.getRoleName());
        GetRoleIdByRoleNamePayload db_role = roleService.getRoleIdByRoleName(getRoleIdByRoleNameInput);
        EmployeeRole db_employeeRole = employeeRoleRepository.findByEmployeeIdAndRoleId(db_employee.getId(),db_role.getRoleId());

        if(db_employee != null &&
                db_role != null &&
                db_employeeRole != null &&
                !db_employee.isDeleted()){
            db_employeeRole.setDeleted(true);
            employeeRoleRepository.save(db_employeeRole);
            log.info(deleteRoleNameFromEmployeeInput.getRoleName() + " deleted from employeeId: " + deleteRoleNameFromEmployeeInput.getEmployeeId());
            return new DeleteRoleNameFromEmployeePayload(true);
        }else{
            return new DeleteRoleNameFromEmployeePayload(false);
        }


    }
}
