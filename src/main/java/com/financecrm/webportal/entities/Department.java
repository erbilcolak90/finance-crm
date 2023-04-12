package com.financecrm.webportal.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("Departments")
public class Department {

    @Id
    private String id;
    private String name;
    private String managerId;
    private boolean isDeleted;
}
