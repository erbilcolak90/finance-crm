package com.financecrm.webportal.entities;

import com.financecrm.webportal.enums.BankAccountStatus;
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
    private String iban;
    private String swiftCode;
    private String bankName;
    private Currency currency;
    private BankAccountStatus status;
    private boolean isDeleted;
    private Date createDate;
    private Date updateDate;

}
