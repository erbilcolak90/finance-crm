package com.financecrm.webportal.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginOutput<T> {

    private T data;
    private String message;
    private boolean success;
}
