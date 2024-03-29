package com.financecrm.webportal.entities;

import com.financecrm.webportal.enums.TransferStatus;
import com.financecrm.webportal.enums.TransferType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("Transfers")
public class Transfer {

    @Id
    private String id;
    private String fromAccountId;
    private String toAccountId;
    private double amount;
    private TransferStatus status;
    private TransferType type;
    private boolean isDeleted;
    private Date createDate;
}
