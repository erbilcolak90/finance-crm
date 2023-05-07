package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.entities.WalletAccount;
import com.financecrm.webportal.enums.Currency;
import com.financecrm.webportal.enums.WalletAccountStatus;
import com.financecrm.webportal.input.walletaccount.GetWalletAccountByUserIdInput;
import com.financecrm.webportal.payload.walletaccount.WalletAccountPayload;
import com.financecrm.webportal.repositories.WalletAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class WalletAccountService {

    @Autowired
    private WalletAccountRepository walletAccountRepository;
    @Autowired
    private CustomUserService customUserService;
    @Autowired
    private MapperService mapperService;


    public WalletAccount getByUserId(String userId){
        return walletAccountRepository.getByUserId(userId);
    }

    public WalletAccount findById(String fromAccountId) {
        return walletAccountRepository.findById(fromAccountId).orElse(null);
    }

    @Transactional
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

    public WalletAccountPayload getWalletAccountByUserId(GetWalletAccountByUserIdInput getWalletAccountByUserIdInput) {
        val db_wallet = walletAccountRepository.getByUserId(getWalletAccountByUserIdInput.getUserId());

        if(db_wallet != null){
            return mapperService.convertToWalletAccountPayload(db_wallet);
        }else{
            return null;
        }
    }

    @Async
    @Transactional
    public void save(WalletAccount db_walletAccount) {
        walletAccountRepository.save(db_walletAccount);
    }
}
