package com.financecrm.webportal.input.bankaccount;

import com.financecrm.webportal.enums.BankName;
import com.financecrm.webportal.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBankAccountInput {

    private String userId;
    private String alias;
    private BankName bankName;
    private String iban;
    private Currency currency;
    private String swiftCode;
}
