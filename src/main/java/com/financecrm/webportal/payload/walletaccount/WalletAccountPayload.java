package com.financecrm.webportal.payload.walletaccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WalletAccountPayload {

    private String id;
    private String userId;
    private double balance;
}
