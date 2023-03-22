package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.TradingAccount;
import com.financecrm.webportal.entities.UserValidationDocument;
import com.financecrm.webportal.payload.tradingaccount.CreateTradingAccountPayload;
import com.financecrm.webportal.payload.tradingaccount.TradingAccountPayload;
import com.financecrm.webportal.payload.uservalidationdocument.UserValidationDocumentPayload;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperService {

    private final ModelMapper modelMapper = new ModelMapper();

    public UserValidationDocumentPayload convertToUserValidationDocumentPayload(UserValidationDocument userValidationDocument){
        return modelMapper.map(userValidationDocument,UserValidationDocumentPayload.class);
    }

    public CreateTradingAccountPayload convertToCreateTradingAccountPayload(TradingAccount tradingAccount){
        return modelMapper.map(tradingAccount, CreateTradingAccountPayload.class);
    }

    public TradingAccountPayload convertToTradingAccountPayload(TradingAccount tradingAccount) {
        return modelMapper.map(tradingAccount,TradingAccountPayload.class);
    }
}
