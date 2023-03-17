package com.financecrm.webportal.entities;

import com.financecrm.webportal.enums.Currency;
import com.financecrm.webportal.enums.TradingAccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("WalletAccounts")
public class WalletAccount {

    @Id
    private String id;
    private String userId;
    private Currency currency;
    private int balance;
    private TradingAccountStatus status;
    private boolean isDeleted;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;

}
