package com.financecrm.webportal.payload.employee;

import com.financecrm.webportal.enums.EmployeeStatus;
import com.financecrm.webportal.enums.JobTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeePayload {

    private String id;
    private String email;
    private String name;
    private String surname;
    private String phone;
    private String departmentId;
    private String teamId;
    private JobTitle jobTitle;
    private EmployeeStatus status;
}
