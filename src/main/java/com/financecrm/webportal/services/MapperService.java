package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.*;
import com.financecrm.webportal.input.user.GetUserByIdInput;
import com.financecrm.webportal.input.userrole.GetUserRolesByUserIdInput;
import com.financecrm.webportal.payload.bankaccount.BankAccountPayload;
import com.financecrm.webportal.payload.bankaccount.CreateBankAccountPayload;
import com.financecrm.webportal.payload.department.CreateDepartmentPayload;
import com.financecrm.webportal.payload.department.DepartmentPayload;
import com.financecrm.webportal.payload.team.CreateTeamPayload;
import com.financecrm.webportal.payload.team.TeamPayload;
import com.financecrm.webportal.payload.tradingaccount.CreateTradingAccountPayload;
import com.financecrm.webportal.payload.tradingaccount.TradingAccountPayload;
import com.financecrm.webportal.payload.transfer.CreateTransferPayload;
import com.financecrm.webportal.payload.transfer.TransferPayload;
import com.financecrm.webportal.payload.user.UserPayload;
import com.financecrm.webportal.payload.uservalidationdocument.UserValidationDocumentPayload;
import com.financecrm.webportal.payload.walletaccount.WalletAccountPayload;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MapperService {

    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private CustomUserService customUserService;
    @Autowired
    private UserRoleService userRoleService;

    public UserValidationDocumentPayload convertToUserValidationDocumentPayload(UserValidationDocument userValidationDocument) {

        return modelMapper.map(userValidationDocument, UserValidationDocumentPayload.class);
    }

    public CreateTradingAccountPayload convertToCreateTradingAccountPayload(TradingAccount tradingAccount) {
        return modelMapper.map(tradingAccount, CreateTradingAccountPayload.class);
    }

    public TradingAccountPayload convertToTradingAccountPayload(TradingAccount tradingAccount) {
        return modelMapper.map(tradingAccount, TradingAccountPayload.class);
    }

    public UserPayload convertToUserPayload(GetUserByIdInput getUserByIdInput) {
        User db_user = customUserService.findByUserId(getUserByIdInput.getUserId());
        if (db_user != null) {
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

    public CreateBankAccountPayload convertToCreateBankAccountPayload(BankAccount bankAccount) {
        return modelMapper.map(bankAccount, CreateBankAccountPayload.class);

    }

    public BankAccountPayload convertToBankAccountPayload(BankAccount bankAccount) {
        return modelMapper.map(bankAccount, BankAccountPayload.class);
    }

    public WalletAccountPayload convertToWalletAccountPayload(WalletAccount walletAccount) {
        return modelMapper.map(walletAccount, WalletAccountPayload.class);
    }

    public TransferPayload convertToGetTransferPayload(Transfer transfer) {
        return modelMapper.map(transfer, TransferPayload.class);
    }

    public CreateTransferPayload convertToCreateTransferPayload(Transfer transfer) {
        return modelMapper.map(transfer, CreateTransferPayload.class);
    }

    public CreateDepartmentPayload convertToCreateDepartmentPayload(Department department) {
        return modelMapper.map(department, CreateDepartmentPayload.class);
    }

    public DepartmentPayload convertToGetDepartmentById(Department department) {
        return modelMapper.map(department, DepartmentPayload.class);
    }

    public DepartmentPayload convertToDepartmentPayload(Department department) {
        return modelMapper.map(department, DepartmentPayload.class);
    }

    public CreateTeamPayload convertToCreateTeamPayload(Team team) {
        return modelMapper.map(team, CreateTeamPayload.class);
    }

    public TeamPayload convertToTeamPayload(Team team) {
        return modelMapper.map(team, TeamPayload.class);
    }
}
