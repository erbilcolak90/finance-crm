package com.financecrm.webportal.event;

import com.financecrm.webportal.enums.TransferType;
import org.springframework.context.ApplicationEvent;

public class TransferEvent extends ApplicationEvent {

    private TransferType transferType;
    private String userId;

    public TransferEvent(Object source, TransferType transferType, String userId) {
        super(source);
        this.transferType = transferType;
        this.userId = userId;
    }

    public TransferType getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
