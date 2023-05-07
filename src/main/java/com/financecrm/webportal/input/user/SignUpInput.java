package com.financecrm.webportal.input.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpInput {

    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;

    //TODO: signupinput içersine role belirlemek için field eklenecek bu field eğer admin ise adminin seçtiği rol signup metodunda eklenecek.
}
