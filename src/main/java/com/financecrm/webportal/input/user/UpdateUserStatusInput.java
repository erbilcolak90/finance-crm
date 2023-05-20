package com.financecrm.webportal.input.user;

import com.financecrm.webportal.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserStatusInput {

    private String userId;
    private UserStatus status;
}
