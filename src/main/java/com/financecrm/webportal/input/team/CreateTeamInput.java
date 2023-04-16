package com.financecrm.webportal.input.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamInput {

    private String name;
    private String departmentId;
    private String managerId;
}
