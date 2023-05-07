package com.financecrm.webportal.controller;

import com.financecrm.webportal.input.tradingaccount.CreateTradingAccountInput;
import com.financecrm.webportal.input.tradingaccount.DeleteTradingAccountInput;
import com.financecrm.webportal.input.tradingaccount.GetAllTradingAccountsByUserIdInput;
import com.financecrm.webportal.input.tradingaccount.GetTradingAccountByIdInput;
import com.financecrm.webportal.payload.tradingaccount.CreateTradingAccountPayload;
import com.financecrm.webportal.payload.tradingaccount.DeleteTradingAccountPayload;
import com.financecrm.webportal.payload.tradingaccount.TradingAccountPayload;
import com.financecrm.webportal.services.TradingAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/tradingAccount")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
public class TradingAccountController {


    private final TradingAccountService tradingAccountService;

    @PostMapping("/getTradingAccountById")
    public ResponseEntity<TradingAccountPayload> getTradingAccountById(@RequestBody GetTradingAccountByIdInput getTradingAccountByIdInput) throws BadCredentialsException {
        TradingAccountPayload result = tradingAccountService.getTradingAccountById(getTradingAccountByIdInput);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            throw new BadCredentialsException("Trading account not found");
        }
    }

    @PostMapping("/getAllTradingAccountsByUserId")
    public ResponseEntity<Page<TradingAccountPayload>> getAllTradingAccountsByUserId(@RequestBody GetAllTradingAccountsByUserIdInput getAllTradingAccountsByUserIdInput) {
        Page<TradingAccountPayload> result = tradingAccountService.getAllTradingAccountsByUserId(getAllTradingAccountsByUserIdInput);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/createTradingAccount")
    public ResponseEntity<CreateTradingAccountPayload> createTradingAccount(@RequestBody CreateTradingAccountInput createTradingAccountInput) throws AccountNotFoundException {
        CreateTradingAccountPayload result = tradingAccountService.createTradingAccount(createTradingAccountInput);
        if (result != null) {
            log.info(createTradingAccountInput.getUserId() + " is create new trading account " + result.getId() + " " + Date.from(Instant.now()));
            return ResponseEntity.ok(result);
        } else {
            throw new AccountNotFoundException("user not found");
        }
    }

    @PostMapping("/deleteTradingAccount")
    public ResponseEntity<DeleteTradingAccountPayload> deleteTradingAccount(@RequestBody DeleteTradingAccountInput deleteTradingAccountInput) throws AccountNotFoundException {
        DeleteTradingAccountPayload result = tradingAccountService.deleteTradingAccount(deleteTradingAccountInput);
        if (result != null) {
            log.info(deleteTradingAccountInput.getId() + " deleted " + Date.from(Instant.now()));
            return ResponseEntity.ok(result);
        } else {
            throw new AccountNotFoundException("user not found");
        }
    }
}
