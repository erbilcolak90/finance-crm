package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.entities.WalletAccount;
import com.financecrm.webportal.enums.Currency;
import com.financecrm.webportal.enums.WalletAccountStatus;
import com.financecrm.webportal.repositories.WalletAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class WalletAccountService {

    @Autowired
    private WalletAccountRepository walletAccountRepository;
    @Autowired
    private CustomUserService customUserService;

    public WalletAccount getByUserId(String userId){
        return walletAccountRepository.getByUserId(userId);
    }

    public void createWalletAccount(String userId){
        User db_user = customUserService.findByUserId(userId);
        if(db_user != null){
            WalletAccount walletAccount = new WalletAccount();
            walletAccount.setBalance(0);
            walletAccount.setCurrency(Currency.USD);
            walletAccount.setStatus(WalletAccountStatus.WAITING);
            walletAccount.setDeleted(false);
            Date date = new Date();
            walletAccount.setCreateDate(date);
            walletAccount.setUpdateDate(date);
            walletAccount.setUserId(userId);

            walletAccountRepository.save(walletAccount);
        }
    }
}
