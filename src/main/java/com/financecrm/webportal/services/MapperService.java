package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.TradingAccount;
import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.entities.UserValidationDocument;
import com.financecrm.webportal.input.user.GetUserByIdInput;
import com.financecrm.webportal.input.userrole.GetUserRolesByUserIdInput;
import com.financecrm.webportal.payload.tradingaccount.CreateTradingAccountPayload;
import com.financecrm.webportal.payload.tradingaccount.TradingAccountPayload;
import com.financecrm.webportal.payload.user.UserPayload;
import com.financecrm.webportal.payload.uservalidationdocument.UserValidationDocumentPayload;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MapperService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private CustomUserService customUserService;
    @Autowired
    private UserRoleService userRoleService;

    public UserValidationDocumentPayload convertToUserValidationDocumentPayload(UserValidationDocument userValidationDocument){

        return modelMapper.map(userValidationDocument,UserValidationDocumentPayload.class);
    }

    public CreateTradingAccountPayload convertToCreateTradingAccountPayload(TradingAccount tradingAccount){
        return modelMapper.map(tradingAccount, CreateTradingAccountPayload.class);
    }

    public TradingAccountPayload convertToTradingAccountPayload(TradingAccount tradingAccount) {
        return modelMapper.map(tradingAccount,TradingAccountPayload.class);
    }

    public UserPayload convertToUserPayload(GetUserByIdInput getUserByIdInput){
        User db_user = customUserService.findByUserId(getUserByIdInput.getUserId());
        if(db_user != null){
            GetUserRolesByUserIdInput getUserRolesByUserIdInput = new GetUserRolesByUserIdInput(db_user.getId());
            List<String> db_userRoles = userRoleService.getUserRolesByUserId(getUserRolesByUserIdInput);
            UserPayload userPayload = new UserPayload();
            userPayload.setId(db_user.getId());
            userPayload.setName(db_user.getName());
            userPayload.setSurname(db_user.getSurname());
            userPayload.setEmail(db_user.getEmail());
            userPayload.setPhone(db_user.getPhone());
            userPayload.setStatus(db_user.getStatus());
            userPayload.setRoles(db_userRoles);
            return userPayload;
        }
        return null;
    }
}
