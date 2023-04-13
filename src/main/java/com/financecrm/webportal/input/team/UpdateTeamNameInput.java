package com.financecrm.webportal.input.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTeamNameInput {

    private String id;
    private String name;
}
