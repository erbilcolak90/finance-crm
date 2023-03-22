package com.financecrm.webportal.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginPayload {

    private String token;
    private String userId;
}
