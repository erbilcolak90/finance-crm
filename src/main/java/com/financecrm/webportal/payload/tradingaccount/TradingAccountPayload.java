package com.financecrm.webportal.payload.tradingaccount;

import com.financecrm.webportal.enums.Currency;
import com.financecrm.webportal.enums.TradingAccountClassification;
import com.financecrm.webportal.enums.TradingAccountStatus;
import com.financecrm.webportal.enums.TradingAccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradingAccountPayload {

    private String id;
    private String userId;
    private String apiId;
    private String walletAccountId;
    private Currency currency;
    private int balance;
    private int leverage;
    private TradingAccountClassification classification;
    private TradingAccountType type;
    private TradingAccountStatus status;
}
