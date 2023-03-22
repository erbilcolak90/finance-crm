package com.financecrm.webportal.payload.tradingaccount;

import com.financecrm.webportal.enums.TradingAccountClassification;
import com.financecrm.webportal.enums.TradingAccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTradingAccountPayload {

    private String id;
    private int leverage;
    private TradingAccountClassification classification;
    private TradingAccountType type;
}
