package com.financecrm.webportal.controller;

import com.financecrm.webportal.input.tradingaccount.CreateTradingAccountInput;
import com.financecrm.webportal.input.tradingaccount.DeleteTradingAccountInput;
import com.financecrm.webportal.input.tradingaccount.GetAllTradingAccountsInput;
import com.financecrm.webportal.input.tradingaccount.GetTradingAccountInput;
import com.financecrm.webportal.payload.tradingaccount.CreateTradingAccountPayload;
import com.financecrm.webportal.payload.tradingaccount.DeleteTradingAccountPayload;
import com.financecrm.webportal.payload.tradingaccount.TradingAccountPayload;
import com.financecrm.webportal.services.TradingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/tradingAccount")
@CrossOrigin
@RequiredArgsConstructor
public class TradingAccountController {

    @Autowired
    private TradingAccountService tradingAccountService;

    @GetMapping("/getTradingAccountById")
    public ResponseEntity<TradingAccountPayload> getTradingAccountById(@RequestBody GetTradingAccountInput getTradingAccountInput) throws Exception {
        TradingAccountPayload result = tradingAccountService.getTradingAccountById(getTradingAccountInput);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            throw new Exception("Trading account not found");
        }
    }

    @PostMapping("/getAllTradingAccountsByUserId")
    public ResponseEntity<Page<TradingAccountPayload>> getAllTradingAccountsByUserId(@RequestBody GetAllTradingAccountsInput getAllTradingAccountsInput){
        Page<TradingAccountPayload> result = tradingAccountService.getAllTradingAccountsByUserId(getAllTradingAccountsInput);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/createTradingAccount")
    public ResponseEntity<CreateTradingAccountPayload> createTradingAccount(@RequestBody CreateTradingAccountInput createTradingAccountInput) throws AccountNotFoundException {
        CreateTradingAccountPayload result = tradingAccountService.createTradingAccount(createTradingAccountInput);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            throw new AccountNotFoundException("user not found");
        }
    }

    @PostMapping("/deleteTradingAccount")
    public ResponseEntity<DeleteTradingAccountPayload> deleteTradingAccount(@RequestBody DeleteTradingAccountInput deleteTradingAccountInput) throws AccountNotFoundException {
        DeleteTradingAccountPayload result = tradingAccountService.deleteTradingAccount(deleteTradingAccountInput);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            throw new AccountNotFoundException("user not found");
        }
    }
}
