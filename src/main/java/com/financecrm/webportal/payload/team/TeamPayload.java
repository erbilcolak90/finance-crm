package com.financecrm.webportal.payload.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamPayload {

    private String id;
    private String name;
    private String departmentId;
    private String managerId;
    private boolean isDeleted;
}
