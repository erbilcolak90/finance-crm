package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.BankAccount;
import com.financecrm.webportal.enums.BankAccountStatus;
import com.financecrm.webportal.input.bankaccount.CreateBankAccountInput;
import com.financecrm.webportal.input.bankaccount.DeleteBankAccountInput;
import com.financecrm.webportal.input.bankaccount.GetAllBankAccountsByUserIdInput;
import com.financecrm.webportal.input.bankaccount.GetBankAccountByIdInput;
import com.financecrm.webportal.payload.bankaccount.BankAccountPayload;
import com.financecrm.webportal.payload.bankaccount.CreateBankAccountPayload;
import com.financecrm.webportal.payload.bankaccount.DeleteBankAccountPayload;
import com.financecrm.webportal.repositories.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private MapperService mapperService;

    @Transactional
    public CreateBankAccountPayload createBankAccount(CreateBankAccountInput createBankAccountInput) {
        var bankAccount = new BankAccount();
        val db_bankAccount = bankAccountRepository.findByIban(createBankAccountInput.getIban());
        val db_user = customUserService.findByUserId(createBankAccountInput.getUserId());

        if (db_bankAccount == null && db_user != null) {
            bankAccount.setUserId(createBankAccountInput.getUserId());
            bankAccount.setAlias(createBankAccountInput.getAlias().toLowerCase());
            bankAccount.setBankName(createBankAccountInput.getBankName());
            bankAccount.setIban(createBankAccountInput.getIban());
            bankAccount.setCurrency(createBankAccountInput.getCurrency());
            bankAccount.setSwiftCode(createBankAccountInput.getSwiftCode());
            bankAccount.setStatus(BankAccountStatus.WAITING);
            bankAccount.setDeleted(false);
            var date = new Date();
            bankAccount.setCreateDate(date);
            bankAccount.setUpdateDate(date);

            bankAccountRepository.save(bankAccount);

            return mapperService.convertToCreateBankAccountPayload(bankAccount);

        } else {
            return null;
        }
    }

    public DeleteBankAccountPayload deleteBankAccount(DeleteBankAccountInput deleteBankAccountInput) {

        var db_bankAccount = bankAccountRepository.findById(deleteBankAccountInput.getId()).orElse(null);
        if (db_bankAccount != null) {
            db_bankAccount.setDeleted(true);
            db_bankAccount.setUpdateDate(new Date());
            bankAccountRepository.save(db_bankAccount);

            return new DeleteBankAccountPayload(true);
        } else {
            return null;
        }
    }

    public BankAccountPayload getBankAccountById(GetBankAccountByIdInput getBankAccountByIdInput) {
        val db_bankAccount = bankAccountRepository.findById(getBankAccountByIdInput.getId()).orElse(null);
        return mapperService.convertToBankAccountPayload(db_bankAccount);
    }
    // TODO: getById' li tüm metodlarda isDeletedFalse olanlar gelecek.

    public Page<BankAccountPayload> getAllBankAccountsByUserId(GetAllBankAccountsByUserIdInput getAllBankAccountsByUserIdInput) {
        Pageable pageable = PageRequest.of(getAllBankAccountsByUserIdInput.getPaginationInput().getPage(),
                getAllBankAccountsByUserIdInput.getPaginationInput().getSize(),
                Sort.by(Sort.Direction.valueOf(getAllBankAccountsByUserIdInput.getPaginationInput().getSortBy().toString()),
                getAllBankAccountsByUserIdInput.getPaginationInput().getFieldName()));
        Page<BankAccount> bankAccountPayloadPage = bankAccountRepository.findByUserIdAndIsDeletedFalse(getAllBankAccountsByUserIdInput.getUserId(),pageable);

        return bankAccountPayloadPage.map(bankAccount -> mapperService.convertToBankAccountPayload(bankAccount));
    }

    public BankAccount findById(String id) {
        return bankAccountRepository.findById(id).orElse(null);
    }
}
