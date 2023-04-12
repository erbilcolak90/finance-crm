package com.financecrm.webportal.payload.department;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentPayload {

    private String id;
    private String name;
    private String managerId;
}
