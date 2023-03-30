package com.financecrm.webportal.input.transfer;

import com.financecrm.webportal.input.PaginationInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllTransfersByUserIdInput {

    private String userId;
    private PaginationInput paginationInput;
}
