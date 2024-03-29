package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.*;
import com.financecrm.webportal.enums.TransferStatus;
import com.financecrm.webportal.enums.TransferType;
import com.financecrm.webportal.enums.WalletAccountStatus;
import com.financecrm.webportal.input.transfer.CreateTransferInput;
import com.financecrm.webportal.input.transfer.DeleteTransferInput;
import com.financecrm.webportal.input.transfer.GetAllTransfersByUserIdInput;
import com.financecrm.webportal.input.transfer.GetTransferByIdInput;
import com.financecrm.webportal.payload.transfer.CreateTransferPayload;
import com.financecrm.webportal.payload.transfer.DeleteTransferPayload;
import com.financecrm.webportal.payload.transfer.TransferPayload;
import com.financecrm.webportal.repositories.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private WalletAccountService walletAccountService;
    @Autowired
    private CustomUserService customUserService;
    @Autowired
    private MapperService mapperService;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private TradingAccountService tradingAccountService;


    public TransferPayload getTransferById(GetTransferByIdInput getTransferByIdInput) {
        Transfer db_transfer = transferRepository.findById(getTransferByIdInput.getId()).orElse(null);

        if (db_transfer != null && !db_transfer.isDeleted()) {
            return mapperService.convertToGetTransferPayload(db_transfer);
        } else {
            return null;
        }
    }

    public Page<TransferPayload> getAllTransfersByUserId(GetAllTransfersByUserIdInput getAllTransfersByUserIdInput) {
        Pageable pageable = PageRequest.of(getAllTransfersByUserIdInput.getPaginationInput().getPage(),
                getAllTransfersByUserIdInput.getPaginationInput().getSize(),
                Sort.by(Sort.Direction.valueOf(getAllTransfersByUserIdInput.getPaginationInput().getSortBy().toString()),
                        getAllTransfersByUserIdInput.getPaginationInput().getFieldName()));
        Page<Transfer> transferPage = transferRepository.findByUserIdAndIsDeletedFalse(getAllTransfersByUserIdInput.getUserId(), pageable);

        return transferPage.map(transfer -> mapperService.convertToGetTransferPayload(transfer));
    }

    @Transactional
    public CreateTransferPayload createTransfer(CreateTransferInput createTransferInput) throws InterruptedException {


        WalletAccount db_walletAccount = null;
        BankAccount db_bankAccount = null;
        TradingAccount db_tradingAccount = null;
        Transfer transfer = null;

        // transfer tipi withdrawsa inputtaki fromAccountId = walletAccount, walletAccount.Amount u ile input.Amount kontrol edilecek
        // transfer işlemi onaya düşmeli direkt transfer işlemi balance lar arasında yapılamaz.
        // ayrıca input.to kullanıcının bankAccountlarından getirilip kontrol edilecek.
        if (createTransferInput.getType().equals(TransferType.WITHDRAW)) {
            db_walletAccount = walletAccountService.findById(createTransferInput.getFromAccountId());
            db_bankAccount = bankAccountService.findById(createTransferInput.getToAccountId());
            if (db_walletAccount != null &&
                    db_bankAccount != null &&
                    !db_bankAccount.isDeleted() &&
                    db_walletAccount.getStatus().equals(WalletAccountStatus.APPROVED) &&
                    db_walletAccount.getBalance() >= createTransferInput.getAmount()) {
                //from wallet to bank
                transfer = new Transfer();
                transfer.setFromAccountId(createTransferInput.getFromAccountId());
                transfer.setToAccountId(createTransferInput.getToAccountId());
                transfer.setType(TransferType.WITHDRAW);
                transfer.setDeleted(false);
                transfer.setStatus(TransferStatus.WAITING);
                // TODO : balance aktarımları DEPOSIT ve WITHDRAW da processler ile birlikte admin onaylarıyla gerçekleşecek.
                // buradaki balance sadece transfer işleminde gerçekleşecek tutarı belirtmekte.
                transfer.setAmount(createTransferInput.getAmount());
                transfer.setCreateDate(new Date());

                transferRepository.save(transfer);
            }
            return mapperService.convertToCreateTransferPayload(transfer);
        }

        // transfer tipi deposit ise fromAccountId = user ın bank accountları ile kıyaslanacak. Amount kontrolü pozitiflik için yapılacak.
        // input.to ise user ın walletAccountIdsine eş değer. direkt WalletAccountBalance ına ekleme yapılacak.
        // transfer işlemi onaya düşmeli direkt transfer işlemi balance lar arasında yapılamaz.
        else if (createTransferInput.getType().equals(TransferType.DEPOSIT)) {
            db_walletAccount = walletAccountService.findById(createTransferInput.getToAccountId());
            db_bankAccount = bankAccountService.findById(createTransferInput.getFromAccountId());
            if (db_bankAccount != null &&
                    db_walletAccount != null &&
                    db_walletAccount.getStatus().equals(WalletAccountStatus.APPROVED) &&
                    !db_bankAccount.isDeleted() &&
                    createTransferInput.getAmount() > 0) {
                transfer = new Transfer();
                //from bank to wallet
                transfer.setFromAccountId(db_bankAccount.getId());
                transfer.setToAccountId(db_walletAccount.getId());
                transfer.setType(TransferType.DEPOSIT);
                transfer.setDeleted(false);
                transfer.setStatus(TransferStatus.WAITING);
                transfer.setAmount(createTransferInput.getAmount());
                transfer.setCreateDate(new Date());

                transferRepository.save(transfer);

            }
            return mapperService.convertToCreateTransferPayload(transfer);
        }
        // transfer tipi virman ise fromAccountId walletid de olabilir tradeAccountId de olabilir.
        // her 2 si için kontrol yapılmalı.
        // burada transfer işlemi sonrası onay gerekmez ancak transfer sonrası 1 dakikalık zaman geçmesi gerekir ki
        // serbest marjindeki paranın durumunda değişmeler olabilir vs.


        else if (createTransferInput.getType().equals(TransferType.VIREMENT_TO_TRADING_ACCOUNT)) {
            db_walletAccount = walletAccountService.findById(createTransferInput.getFromAccountId());
            db_tradingAccount = tradingAccountService.findById(createTransferInput.getToAccountId());
            // from wallet to tradingAccount
            if (db_walletAccount != null &&
                    db_tradingAccount != null &&
                    !db_tradingAccount.isDeleted() &&
                    db_walletAccount.getStatus().equals(WalletAccountStatus.APPROVED) &&
                    db_walletAccount.getBalance() >= createTransferInput.getAmount()) {
                transfer = new Transfer();
                transfer.setFromAccountId(db_walletAccount.getId());
                transfer.setToAccountId(db_tradingAccount.getId());
                transfer.setType(TransferType.VIREMENT_TO_TRADING_ACCOUNT);
                transfer.setDeleted(false);
                transfer.setStatus(TransferStatus.APPROVED);
                transfer.setAmount(createTransferInput.getAmount());
                transfer.setCreateDate(new Date());
                transferRepository.save(transfer);

                db_tradingAccount.setBalance(db_tradingAccount.getBalance() + transfer.getAmount());
                db_tradingAccount.setUpdateDate(new Date());
                tradingAccountService.save(db_tradingAccount);

                db_walletAccount.setBalance(db_walletAccount.getBalance() - transfer.getAmount());
                db_walletAccount.setUpdateDate(new Date());
                walletAccountService.save(db_walletAccount);

            }
            return mapperService.convertToCreateTransferPayload(transfer);

        }
        else if (createTransferInput.getType().equals(TransferType.VIREMENT_TO_WALLET)) {
            db_walletAccount = walletAccountService.findById(createTransferInput.getToAccountId());
            db_tradingAccount = tradingAccountService.findById(createTransferInput.getFromAccountId());

            if (db_walletAccount != null &&
                    db_tradingAccount != null &&
                    !db_tradingAccount.isDeleted() &&
                    db_walletAccount.getStatus().equals(WalletAccountStatus.APPROVED) &&
                    db_tradingAccount.getBalance() >= createTransferInput.getAmount()) {
                transfer = new Transfer();
                transfer.setFromAccountId(db_tradingAccount.getId());
                transfer.setToAccountId(db_walletAccount.getId());
                transfer.setType(TransferType.VIREMENT_TO_WALLET);
                transfer.setDeleted(false);
                transfer.setStatus(TransferStatus.APPROVED);
                transfer.setAmount(createTransferInput.getAmount());
                transfer.setCreateDate(new Date());

                setVirementToWalletBalance(createTransferInput.getAmount(), db_tradingAccount.getId(), db_walletAccount.getId(), transfer);


            }
            return mapperService.convertToCreateTransferPayload(transfer);
        } else {
            return null;
        }
    }

    @Transactional
    public DeleteTransferPayload deleteTransfer(DeleteTransferInput deleteTransferInput) {
        Transfer transfer = transferRepository.findById(deleteTransferInput.getId()).orElse(null);
        if (transfer != null &&
                transfer.getType().equals(TransferType.DEPOSIT) &&
                !transfer.getStatus().equals(TransferStatus.APPROVED)) {
            transfer.setDeleted(true);
            transfer.setStatus(TransferStatus.DENIED);
            transferRepository.save(transfer);

            return new DeleteTransferPayload(true);
        } else {
            return new DeleteTransferPayload(false);
        }

    }

    @Async
    public void setVirementToWalletBalance(double amount, String db_tradingAccountId, String db_walletAccountId, Transfer transfer) throws InterruptedException {

        Thread.sleep(5_000);
        TradingAccount db_tradingAccount = tradingAccountService.findById(db_tradingAccountId);
        WalletAccount db_walletAccount = walletAccountService.findById(db_walletAccountId);


        if (db_tradingAccount.getBalance() >= amount) {

            transferRepository.save(transfer);

            db_tradingAccount.setBalance(db_tradingAccount.getBalance() - amount);
            db_tradingAccount.setUpdateDate(new Date());
            tradingAccountService.save(db_tradingAccount);

            db_walletAccount.setBalance(db_walletAccount.getBalance() + amount);
            db_walletAccount.setUpdateDate(new Date());
            walletAccountService.save(db_walletAccount);

        } else {
            throw new RuntimeException("not enough balance ");
        }


    }
}
