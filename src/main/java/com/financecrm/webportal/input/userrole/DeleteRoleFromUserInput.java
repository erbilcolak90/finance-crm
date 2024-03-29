package com.financecrm.webportal.input.userrole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteRoleFromUserInput {

    private String userId;
    private String roleName;
}
