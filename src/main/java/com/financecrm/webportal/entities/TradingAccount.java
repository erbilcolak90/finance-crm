package com.financecrm.webportal.entities;

import com.financecrm.webportal.enums.Currency;
import com.financecrm.webportal.enums.TradingAccountClassification;
import com.financecrm.webportal.enums.TradingAccountStatus;
import com.financecrm.webportal.enums.TradingAccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("TradingAccounts")
public class TradingAccount {

    @Id
    private String id;
    private String userId;
    private String apiId;
    private Currency currency;
    private int balance;
    private int leverage;
    private TradingAccountClassification classification;
    private TradingAccountType type;
    private TradingAccountStatus status;
    private boolean isDeleted;
    private Date createDate;
    private Date updateDate;

}
