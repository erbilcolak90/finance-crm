package com.financecrm.webportal.input.tradingaccount;

import com.financecrm.webportal.enums.SortBy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllTradingAccountsInput {

    private String userId;
    private int page;
    private int size;
    private String fieldName;
    private SortBy sortBy;
}
