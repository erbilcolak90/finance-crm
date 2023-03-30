package com.financecrm.webportal.payload.bankaccount;

import com.financecrm.webportal.enums.BankAccountStatus;
import com.financecrm.webportal.enums.BankName;
import com.financecrm.webportal.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountPayload {

    private String id;
    private String userId;
    private String alias;
    private BankName bankName;
    private String iban;
    private Currency currency;
    private String swiftCode;
    private BankAccountStatus status;
    private Date createDate;
}
