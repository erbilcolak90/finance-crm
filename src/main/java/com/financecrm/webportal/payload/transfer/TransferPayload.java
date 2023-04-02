package com.financecrm.webportal.payload.transfer;

import com.financecrm.webportal.enums.TransferStatus;
import com.financecrm.webportal.enums.TransferType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferPayload {

    private String id;
    private String fromAccountId;
    private String toAccountId;
    private double amount;
    private TransferStatus status;
    private TransferType type;
    private Date createDate;


}
