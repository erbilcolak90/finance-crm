package com.financecrm.webportal.event.listeners;

import com.financecrm.webportal.enums.TransferType;
import com.financecrm.webportal.event.TransferEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransferListener {

    @EventListener
    public void handleUserEvent(TransferEvent event) {
        if (event.getTransferType().equals(TransferType.WITHDRAW)) {
            handleWithdrawTransfer(event.getUserId());
        } else if (event.getTransferType().equals(TransferType.DEPOSIT)) {
            handleDepositTransfer(event.getUserId());
        } else if (event.getTransferType().equals(TransferType.VIREMENT_TO_WALLET)) {
            handleVirementToWallet(event.getUserId());
        }else{
            handleVirementToTradingAccount(event.getUserId());
        }
    }

    public void handleWithdrawTransfer(String userId){
        // bu işlem hangi admine veya operasyona atandıysa onunla ilgili bilgilendirme koyabiliriz.
        log.info(userId + " create to withdraw transfer");
    }

    public void handleDepositTransfer(String userId){
        // bu işlem hangi admine veya operasyona atandıysa onunla ilgili bilgilendirme koyabiliriz.
        log.info(userId + " create to deposit transfer");
    }

    public void handleVirementToWallet(String userId){
        log.info(userId + " virement to wallet");
    }
    public void handleVirementToTradingAccount(String userId){
        log.info(userId + " virement to trading account");
    }

}
