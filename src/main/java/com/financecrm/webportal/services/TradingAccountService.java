package com.financecrm.webportal.services;

import com.financecrm.webportal.auth.TokenManager;
import com.financecrm.webportal.entities.TradingAccount;
import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.entities.WalletAccount;
import com.financecrm.webportal.enums.TradingAccountStatus;
import com.financecrm.webportal.enums.UserStatus;
import com.financecrm.webportal.enums.WalletAccountStatus;
import com.financecrm.webportal.input.PaginationInput;
import com.financecrm.webportal.input.tradingaccount.CreateTradingAccountInput;
import com.financecrm.webportal.input.tradingaccount.DeleteTradingAccountInput;
import com.financecrm.webportal.input.tradingaccount.GetAllTradingAccountsInput;
import com.financecrm.webportal.input.tradingaccount.GetTradingAccountInput;
import com.financecrm.webportal.payload.tradingaccount.CreateTradingAccountPayload;
import com.financecrm.webportal.payload.tradingaccount.DeleteTradingAccountPayload;
import com.financecrm.webportal.payload.tradingaccount.TradingAccountPayload;
import com.financecrm.webportal.repositories.TradingAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TradingAccountService {

    @Autowired
    private TradingAccountRepository tradingAccountRepository;
    @Autowired
    private CustomUserService customUserService;
    @Autowired
    private WalletAccountService walletAccountService;
    @Autowired
    private MapperService mapperService;
    @Autowired
    private TokenManager tokenManager;

    @Transactional
    public CreateTradingAccountPayload createTradingAccount(CreateTradingAccountInput createTradingAccountInput) {
        User db_user = customUserService.findByUserId(createTradingAccountInput.getUserId());
        WalletAccount db_walletAccount = walletAccountService.getByUserId(createTradingAccountInput.getUserId());
        if (db_user != null
                && db_user.getStatus() == UserStatus.APPROVED
                && !db_user.isDeleted()
                && db_walletAccount.getStatus() == WalletAccountStatus.APPROVED
                && !db_walletAccount.isDeleted()) {
            TradingAccount tradingAccount = new TradingAccount();
            tradingAccount.setUserId(createTradingAccountInput.getUserId());
            tradingAccount.setApiId(UUID.randomUUID().toString());
            tradingAccount.setWalletAccountId(db_walletAccount.getId());
            tradingAccount.setCurrency(createTradingAccountInput.getCurrency());
            tradingAccount.setBalance(0);
            tradingAccount.setLeverage(createTradingAccountInput.getLeverage());
            tradingAccount.setClassification(createTradingAccountInput.getClassification());
            tradingAccount.setType(createTradingAccountInput.getType());
            tradingAccount.setStatus(TradingAccountStatus.WAITING);
            tradingAccount.setDeleted(false);
            Date date = new Date();
            tradingAccount.setCreateDate(date);
            tradingAccount.setUpdateDate(date);

            tradingAccountRepository.save(tradingAccount);

            return mapperService.convertToCreateTradingAccountPayload(tradingAccount);

        } else {
            return null;
        }
    }

    @Transactional
    public DeleteTradingAccountPayload deleteTradingAccount(DeleteTradingAccountInput deleteTradingAccountInput) {

        TradingAccount db_tradingAccount = tradingAccountRepository.findById(deleteTradingAccountInput.getId()).orElse(null);
        if (db_tradingAccount != null) {
            db_tradingAccount.setDeleted(true);
            db_tradingAccount.setUpdateDate(new Date());
            tradingAccountRepository.save(db_tradingAccount);
            return new DeleteTradingAccountPayload(true);
        } else {
            return new DeleteTradingAccountPayload(false);
        }

    }

    public TradingAccountPayload getTradingAccountById(GetTradingAccountInput getTradingAccountInput) {
        TradingAccountPayload payload = null;
        Optional<TradingAccount> db_tradingAccount = tradingAccountRepository.findById(getTradingAccountInput.getId());
        if (db_tradingAccount.isPresent()) {
            payload = mapperService.convertToTradingAccountPayload(db_tradingAccount.get());
        }
        return payload;
    }

    public Page<TradingAccountPayload> getAllTradingAccountsByUserId(GetAllTradingAccountsInput getAllTradingAccountsInput) {
        Pageable pageable = PageRequest.of(getAllTradingAccountsInput.getPage(), getAllTradingAccountsInput.getSize(), Sort.by(Sort.Direction.valueOf(getAllTradingAccountsInput.getSortBy().toString()), getAllTradingAccountsInput.getFieldName()));
        Page<TradingAccount> tradingAccountPage = tradingAccountRepository.getAllTradingAccountsByUserId(getAllTradingAccountsInput.getUserId(),pageable);
        return tradingAccountPage.map(tradingAccount -> mapperService.convertToTradingAccountPayload(tradingAccount));

    }
}
