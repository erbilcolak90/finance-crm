package com.financecrm.webportal.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.financecrm.webportal.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("Users")
public class User {

    @Id
    private String id;
    private String email;
    @JsonIgnore
    private String password;
    private String name;
    private String surname;
    private String phone;
    private String representativeEmployeeId;
    private UserStatus status;
    private boolean isDeleted;
    private Date createDate;
    private Date updateDate;
}
