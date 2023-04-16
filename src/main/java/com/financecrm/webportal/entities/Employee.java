package com.financecrm.webportal.entities;

import com.financecrm.webportal.enums.EmployeeStatus;
import com.financecrm.webportal.enums.JobTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("Employees")
public class Employee {

    @Id
    private String id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phone;
    private String departmentId;
    private String teamId;
    private JobTitle jobTitle;
    private EmployeeStatus status;
    private boolean isDeleted;
    private Date createDate;
    private Date updateDate;
}
