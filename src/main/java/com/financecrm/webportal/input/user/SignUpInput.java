package com.financecrm.webportal.input.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpInput {

    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
    private Date date = new Date();
}
