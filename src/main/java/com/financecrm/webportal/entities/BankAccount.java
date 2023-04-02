package com.financecrm.webportal.entities;

import com.financecrm.webportal.enums.BankAccountStatus;
import com.financecrm.webportal.enums.BankName;
import com.financecrm.webportal.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("BankAccounts")
public class BankAccount {

    @Id
    private String id;
    private String userId;
    private String alias;
    private BankName bankName;
    private String iban;
    private Currency currency;
    private String swiftCode;
    private BankAccountStatus status;
    private boolean isDeleted;
    private Date createDate;
    private Date updateDate;

}
