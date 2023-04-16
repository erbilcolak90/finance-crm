package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.Employee;
import com.financecrm.webportal.enums.EmployeeStatus;
import com.financecrm.webportal.input.department.GetDepartmentByIdInput;
import com.financecrm.webportal.input.employee.*;
import com.financecrm.webportal.input.team.GetTeamByIdInput;
import com.financecrm.webportal.payload.department.DepartmentPayload;
import com.financecrm.webportal.payload.employee.CreateEmployeePayload;
import com.financecrm.webportal.payload.employee.DeleteEmployeePayload;
import com.financecrm.webportal.payload.employee.EmployeePayload;
import com.financecrm.webportal.payload.team.TeamPayload;
import com.financecrm.webportal.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MapperService mapperService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Employee findById(String managerId) {
        return employeeRepository.findById(managerId).orElse(null);
    }

    public void save(Employee db_employee) {
        employeeRepository.save(db_employee);
    }

    @Transactional
    public CreateEmployeePayload createEmployee(CreateEmployeeInput createEmployeeInput) {

        Employee db_employee = employeeRepository.findByEmail(createEmployeeInput.getEmail().toLowerCase());
        GetDepartmentByIdInput getDepartmentByIdInput = new GetDepartmentByIdInput(createEmployeeInput.getDepartmentId());
        DepartmentPayload departmentPayload = departmentService.getDepartmentById(getDepartmentByIdInput);

        GetTeamByIdInput getTeamByIdInput = new GetTeamByIdInput(createEmployeeInput.getTeamId());
        TeamPayload teamPayload = teamService.getTeamById(getTeamByIdInput);

        if (db_employee == null &&
                departmentPayload != null &&
                teamPayload != null) {
            Employee employee = new Employee();
            employee.setEmail(createEmployeeInput.getEmail().toLowerCase());
            employee.setPassword(bCryptPasswordEncoder.encode(createEmployeeInput.getPassword()));
            employee.setName(createEmployeeInput.getName());
            employee.setSurname(createEmployeeInput.getSurname());
            employee.setPhone(createEmployeeInput.getPhone());
            employee.setDepartmentId(createEmployeeInput.getDepartmentId());
            employee.setTeamId(createEmployeeInput.getTeamId());
            employee.setJobTitle(createEmployeeInput.getJobTitle());
            employee.setStatus(EmployeeStatus.WAITING);
            Date date = new Date();
            employee.setCreateDate(date);
            employee.setUpdateDate(date);
            employee.setDeleted(false);

            employeeRepository.save(employee);
            log.info("employee created " + employee.getId());

            return mapperService.convertToCreateEmployeePayload(employee);

        } else {
            if (db_employee != null) {
                log.info("this email is already exist");
            } else if (departmentPayload == null) {
                log.info("Department not found");
            } else if (teamPayload == null) {
                log.info("Team not found");
            } else {
                log.info("manager already deleted");
            }
            return null;
        }

    }

    @Transactional
    public DeleteEmployeePayload deleteEmployee(DeleteEmployeeInput deleteEmployeeInput) {

        Employee db_employee = employeeRepository.findById(deleteEmployeeInput.getId()).orElse(null);

        if (db_employee != null && !db_employee.isDeleted()) {
            db_employee.setDeleted(true);
            db_employee.setUpdateDate(new Date());
            employeeRepository.save(db_employee);
            log.info(db_employee.getId() + " employee deleted");

            return new DeleteEmployeePayload(true);

        } else {
            if (db_employee == null) {
                log.info("employee not found");

            } else {
                log.info("employee is already deleted");
            }
        }
        return null;
    }


    public EmployeePayload getEmployeeById(GetEmployeeByIdInput getEmployeeByIdInput) {

        Employee employee = employeeRepository.findById(getEmployeeByIdInput.getId()).orElse(null);

        return mapperService.convertToEmployeePayload(employee);


    }

    public Page<EmployeePayload> getAllEmployees(GetAllEmployeesInput getAllEmployeesInput) {
        Pageable pageable = PageRequest.of(getAllEmployeesInput.getPagination().getPage(),
                getAllEmployeesInput.getPagination().getSize(),
                Sort.by(Sort.Direction.valueOf(getAllEmployeesInput.getPagination().getSortBy().toString()), getAllEmployeesInput.getPagination().getFieldName()));
        Page<Employee> employeePage = employeeRepository.findByIsDeletedFalse(pageable);

        return employeePage.map(employee -> mapperService.convertToEmployeePayload(employee));
    }

    public EmployeePayload updateEmployeeInput(UpdateEmployeeInput updateEmployeeInput) {
        Employee db_employee = employeeRepository.findById(updateEmployeeInput.getId()).orElse(null);

        if (db_employee != null && !db_employee.isDeleted()) {
            if (updateEmployeeInput.getEmail() != null && !updateEmployeeInput.getEmail().toLowerCase().equals(db_employee.getEmail())) {
                db_employee.setEmail(updateEmployeeInput.getEmail());
            }
            if (updateEmployeeInput.getName() != null && !updateEmployeeInput.getName().equals(db_employee.getName())) {
                db_employee.setName(updateEmployeeInput.getName());
            }
            if (updateEmployeeInput.getPhone() != null && !updateEmployeeInput.getPhone().equals(db_employee.getPhone())) {
                db_employee.setPhone(updateEmployeeInput.getPhone());
            }
            if (updateEmployeeInput.getTeamId() != null && !updateEmployeeInput.getTeamId().equals(db_employee.getTeamId())) {
                GetTeamByIdInput getTeamByIdInput = new GetTeamByIdInput(updateEmployeeInput.getTeamId());
                TeamPayload db_team = teamService.getTeamById(getTeamByIdInput);
                if (db_team != null) {
                    db_employee.setTeamId(updateEmployeeInput.getTeamId());
                }
            }
            return mapperService.convertToEmployeePayload(db_employee);
        } else {
            return null;
        }
    }
}
