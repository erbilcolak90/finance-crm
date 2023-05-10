package com.financecrm.webportal.input.bankaccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteBankAccountInput {

    private String id;
    private Date date = new Date();
}
