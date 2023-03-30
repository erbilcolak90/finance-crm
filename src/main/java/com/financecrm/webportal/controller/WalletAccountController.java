package com.financecrm.webportal.controller;

import com.financecrm.webportal.input.walletaccount.GetWalletAccountByUserIdInput;
import com.financecrm.webportal.payload.walletaccount.WalletAccountPayload;
import com.financecrm.webportal.services.WalletAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/walletAccount")
@RequiredArgsConstructor
public class WalletAccountController {

    @Autowired
    private WalletAccountService walletAccountService;

    @GetMapping("/getWalletAccountByUserId")
    public ResponseEntity<WalletAccountPayload> getWalletAccountByUserId(@RequestBody GetWalletAccountByUserIdInput getWalletAccountByUserIdInput){
        WalletAccountPayload result = walletAccountService.getWalletAccountByUserId(getWalletAccountByUserIdInput);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            throw new BadCredentialsException("User or Wallet not found");
        }
    }
}
