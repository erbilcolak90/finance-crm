package com.financecrm.webportal.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("UserRoles")
public class UserRole {

    @Id
    private String id;
    private String roleId;
    private String userId;
    private boolean isDeleted;
}
