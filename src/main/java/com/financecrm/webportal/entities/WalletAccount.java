package com.financecrm.webportal.entities;

import com.financecrm.webportal.enums.Currency;
import com.financecrm.webportal.enums.WalletAccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("WalletAccounts")
public class WalletAccount {

    @Id
    private String id;
    private String userId;
    private Currency currency;
    private double balance;
    private WalletAccountStatus status;
    private boolean isDeleted;
    private Date createDate;
    private Date updateDate;

}
