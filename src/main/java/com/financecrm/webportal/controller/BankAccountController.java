package com.financecrm.webportal.controller;

import com.financecrm.webportal.input.bankaccount.CreateBankAccountInput;
import com.financecrm.webportal.input.bankaccount.DeleteBankAccountInput;
import com.financecrm.webportal.input.bankaccount.GetAllBankAccountsByUserIdInput;
import com.financecrm.webportal.input.bankaccount.GetBankAccountByIdInput;
import com.financecrm.webportal.payload.bankaccount.BankAccountPayload;
import com.financecrm.webportal.payload.bankaccount.CreateBankAccountPayload;
import com.financecrm.webportal.payload.bankaccount.DeleteBankAccountPayload;
import com.financecrm.webportal.services.BankAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bankAccount")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping("/createBankAccount")
    public ResponseEntity<CreateBankAccountPayload> createBankAccount(@RequestBody CreateBankAccountInput createBankAccountInput){
        CreateBankAccountPayload result= bankAccountService.createBankAccount(createBankAccountInput);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return null;
        }
    }

    @PostMapping("/deleteBankAccount")
    public ResponseEntity<DeleteBankAccountPayload> deleteBankAccount(@RequestBody DeleteBankAccountInput deleteBankAccountInput){
        DeleteBankAccountPayload result = bankAccountService.deleteBankAccount(deleteBankAccountInput);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return null;
        }
    }

    @PostMapping("/getBankAccountById")
    public ResponseEntity<BankAccountPayload> getBankAccountById(@RequestBody GetBankAccountByIdInput getBankAccountByIdInput){
        BankAccountPayload result = bankAccountService.getBankAccountById(getBankAccountByIdInput);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return null;
        }
    }

    @PostMapping("/getAllBankAccountsByUserId")
    public Page<BankAccountPayload> getAllBankAccountsByUserId(@RequestBody GetAllBankAccountsByUserIdInput getAllBankAccountsByUserIdInput){
        return bankAccountService.getAllBankAccountsByUserId(getAllBankAccountsByUserIdInput);
    }




}
