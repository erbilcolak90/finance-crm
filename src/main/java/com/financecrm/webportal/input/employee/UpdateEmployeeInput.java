package com.financecrm.webportal.input.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeInput {

    private String id;
    private String email;
    private String name;
    private String phone;
    private String managerId;
    private String teamId;


}
