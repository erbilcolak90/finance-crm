package com.financecrm.webportal.input.bankaccount;

import com.financecrm.webportal.input.PaginationInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllBankAccountsByUserIdInput {

    private String userId;

    private PaginationInput paginationInput;
}
