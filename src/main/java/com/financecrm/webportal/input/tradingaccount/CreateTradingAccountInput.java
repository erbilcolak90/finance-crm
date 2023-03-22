package com.financecrm.webportal.input.tradingaccount;


import com.financecrm.webportal.enums.Currency;
import com.financecrm.webportal.enums.TradingAccountClassification;
import com.financecrm.webportal.enums.TradingAccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTradingAccountInput {

    private String userId;
    private String apiId;
    private Currency currency;
    private int leverage;
    private TradingAccountClassification classification;
    private TradingAccountType type;
}
