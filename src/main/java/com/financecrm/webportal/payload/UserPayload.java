package com.financecrm.webportal.payload;

import com.financecrm.webportal.entities.UserRole;
import com.financecrm.webportal.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPayload {

    private String id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private UserStatus status;
    private List<String> roles;
}
