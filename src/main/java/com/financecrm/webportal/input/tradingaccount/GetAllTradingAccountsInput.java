package com.financecrm.webportal.input.tradingaccount;

import com.financecrm.webportal.input.PaginationInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllTradingAccountsInput {

    private String userId;
    private PaginationInput paginationInput;
}
