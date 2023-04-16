package com.financecrm.webportal.input.employeerole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRoleNameFromEmployeeInput {

    private String roleName;
    private String employeeId;
}
