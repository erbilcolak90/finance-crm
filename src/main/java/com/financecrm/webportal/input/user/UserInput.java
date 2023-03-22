package com.financecrm.webportal.input.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInput {

    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
}
