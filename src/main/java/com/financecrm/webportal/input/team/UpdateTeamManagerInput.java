package com.financecrm.webportal.input.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTeamManagerInput {

    private String managerId;
    private String teamId;
}
