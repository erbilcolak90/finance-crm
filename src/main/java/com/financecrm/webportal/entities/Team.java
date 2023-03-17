package com.financecrm.webportal.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("Teams")
public class Team {

    @Id
    private String id;
    private String name;
    private String departmentId;
    private String managerId;

}
