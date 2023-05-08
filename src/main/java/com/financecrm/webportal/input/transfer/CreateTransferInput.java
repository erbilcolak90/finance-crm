package com.financecrm.webportal.input.transfer;

import com.financecrm.webportal.enums.TransferType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransferInput {

    private String userId;
    private String fromAccountId;
    private String toAccountId;
    private double amount;
    private TransferType type;
    private Date date = new Date();
}
