package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.Employee;
import com.financecrm.webportal.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee findById(String managerId) {
        return employeeRepository.findById(managerId).orElse(null);
    }
}
