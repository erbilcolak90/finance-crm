package com.financecrm.webportal;

import com.financecrm.webportal.entities.TradingAccount;
import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.entities.WalletAccount;
import com.financecrm.webportal.enums.*;
import com.financecrm.webportal.input.PaginationInput;
import com.financecrm.webportal.input.tradingaccount.CreateTradingAccountInput;
import com.financecrm.webportal.input.tradingaccount.DeleteTradingAccountInput;
import com.financecrm.webportal.input.tradingaccount.GetAllTradingAccountsByUserIdInput;
import com.financecrm.webportal.input.tradingaccount.GetTradingAccountByIdInput;
import com.financecrm.webportal.payload.tradingaccount.CreateTradingAccountPayload;
import com.financecrm.webportal.payload.tradingaccount.DeleteTradingAccountPayload;
import com.financecrm.webportal.payload.tradingaccount.TradingAccountPayload;
import com.financecrm.webportal.repositories.TradingAccountRepository;
import com.financecrm.webportal.services.CustomUserService;
import com.financecrm.webportal.services.MapperService;
import com.financecrm.webportal.services.TradingAccountService;
import com.financecrm.webportal.services.WalletAccountService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TradingAccountServiceTest {

    @InjectMocks
    private TradingAccountService tradingAccountService;
    @Mock
    private TradingAccountRepository tradingAccountRepository;
    @Mock
    private CustomUserService customUserService;
    @Mock
    private WalletAccountService walletAccountService;
    @Mock
    private MapperService mapperService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("should Return CreateTradingAccountPayload when User IsExist And UserStatus Is Approved And User IsDeleted False And WalletAccount Is Exist And Status Is Approved And WalletAccount IsDeleted False From CreateTradingAccountInput")
    @Tag("createTradingAccount")
    @Test
    void shouldReturnCreateTradingAccountPayload_whenUserIsExistAndUserStatusIsApprovedAndUserIsDeletedFalseAndWalletAccountIsExistAndStatusIsApprovedAndWalletAccountIsDeletedFalseFromCreateTradingAccountInput(){
        CreateTradingAccountInput createTradingAccountInput = new CreateTradingAccountInput("test_userId","test_apiId", Currency.TEST,100, TradingAccountClassification.TEST, TradingAccountType.TEST,new Date());
        User db_user = new User("test_userId","test_email","test_password","test_name","test_surname","test_phone","test_representativeEmployeeId", UserStatus.APPROVED,false,new Date(),new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletId","test_userId",Currency.TEST,0, WalletAccountStatus.APPROVED,false,new Date(),new Date());
        TradingAccount tradingAccount = new TradingAccount("test_tradingAccountId","test_userId","test_apiId","test_walletAccountId",Currency.TEST,0,100,TradingAccountClassification.TEST,TradingAccountType.TEST,TradingAccountStatus.WAITING,false,createTradingAccountInput.getDate(),createTradingAccountInput.getDate());
        CreateTradingAccountPayload result = new CreateTradingAccountPayload("test_tradingAccountId",0,TradingAccountClassification.TEST,TradingAccountType.TEST);

        Mockito.when(customUserService.findByUserId(createTradingAccountInput.getUserId())).thenReturn(db_user);
        Mockito.when(walletAccountService.getByUserId(createTradingAccountInput.getUserId())).thenReturn(db_walletAccount);
        Mockito.when(tradingAccountRepository.save(ArgumentMatchers.any(TradingAccount.class))).thenReturn(tradingAccount);
        Mockito.when(mapperService.convertToCreateTradingAccountPayload(tradingAccount)).thenReturn(result);

        CreateTradingAccountPayload expectedResult = tradingAccountService.createTradingAccount(createTradingAccountInput);

        assertSame(expectedResult,result);

        Mockito.verify(customUserService).findByUserId(createTradingAccountInput.getUserId());
        Mockito.verify(walletAccountService).getByUserId(createTradingAccountInput.getUserId());
        Mockito.verify(tradingAccountRepository).save(ArgumentMatchers.any(TradingAccount.class));
        Mockito.verify(mapperService).convertToCreateTradingAccountPayload(tradingAccount);

    }

    @DisplayName("should Return Null when User Does Not Exist From CreateTradingAccountInput")
    @Tag("createTradingAccount")
    @Test
    void shouldReturnNull_whenUserDoesNotExistFromCreateTradingAccountInput(){
        CreateTradingAccountInput createTradingAccountInput = new CreateTradingAccountInput("test_userId","test_apiId", Currency.TEST,100, TradingAccountClassification.TEST, TradingAccountType.TEST,new Date());

        Mockito.when(customUserService.findByUserId(createTradingAccountInput.getUserId())).thenReturn(null);
        Mockito.when(walletAccountService.getByUserId(createTradingAccountInput.getUserId())).thenReturn(null);

        CreateTradingAccountPayload expectedResult = tradingAccountService.createTradingAccount(createTradingAccountInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTradingAccountInput.getUserId());
        Mockito.verify(walletAccountService).getByUserId(createTradingAccountInput.getUserId());

    }

    @DisplayName("should Return Null when User Is Exist And Wallet Account Does Not Exist From CreateTradingAccountInput")
    @Tag("createTradingAccount")
    @Test
    void shouldReturnNull_whenUserIsExistAndWalletAccountDoesNotExistFromCreateTradingAccountInput(){
        CreateTradingAccountInput createTradingAccountInput = new CreateTradingAccountInput("test_userId","test_apiId", Currency.TEST,100, TradingAccountClassification.TEST, TradingAccountType.TEST,new Date());
        User db_user = new User("test_userId","test_email","test_password","test_name","test_surname","test_phone","test_representativeEmployeeId", UserStatus.APPROVED,false,new Date(),new Date());

        Mockito.when(customUserService.findByUserId(createTradingAccountInput.getUserId())).thenReturn(db_user);
        Mockito.when(walletAccountService.getByUserId(createTradingAccountInput.getUserId())).thenReturn(null);

        CreateTradingAccountPayload expectedResult = tradingAccountService.createTradingAccount(createTradingAccountInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTradingAccountInput.getUserId());
        Mockito.verify(walletAccountService).getByUserId(createTradingAccountInput.getUserId());

    }

    @DisplayName("should Return Null when User Is Exist And IsDeleted True And WalletAccount IsExist From CreateTradingAccountInput")
    @Tag("createTradingAccount")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedTrueAndWalletAccountIsExistFromCreateTradingAccountInput(){
        CreateTradingAccountInput createTradingAccountInput = new CreateTradingAccountInput("test_userId","test_apiId", Currency.TEST,100, TradingAccountClassification.TEST, TradingAccountType.TEST,new Date());
        User db_user = new User("test_userId","test_email","test_password","test_name","test_surname","test_phone","test_representativeEmployeeId", UserStatus.APPROVED,true,new Date(),new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletId","test_userId",Currency.TEST,0, WalletAccountStatus.APPROVED,false,new Date(),new Date());

        Mockito.when(customUserService.findByUserId(createTradingAccountInput.getUserId())).thenReturn(db_user);
        Mockito.when(walletAccountService.getByUserId(createTradingAccountInput.getUserId())).thenReturn(db_walletAccount);

        CreateTradingAccountPayload expectedResult = tradingAccountService.createTradingAccount(createTradingAccountInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTradingAccountInput.getUserId());
        Mockito.verify(walletAccountService).getByUserId(createTradingAccountInput.getUserId());

    }

    @DisplayName("should Return Null when User IsExist And IsDeleted False And UserStatus Not Approved And WalletAccount Is Exist From CreateTradingAccountInput")
    @Tag("createTradingAccount")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndUserStatusNotApprovedAndWalletAccountIsExistFromCreateTradingAccountInput(){
        CreateTradingAccountInput createTradingAccountInput = new CreateTradingAccountInput("test_userId","test_apiId", Currency.TEST,100, TradingAccountClassification.TEST, TradingAccountType.TEST,new Date());
        User db_user = new User("test_userId","test_email","test_password","test_name","test_surname","test_phone","test_representativeEmployeeId", UserStatus.TEST,false,new Date(),new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletId","test_userId",Currency.TEST,0, WalletAccountStatus.APPROVED,false,new Date(),new Date());

        Mockito.when(customUserService.findByUserId(createTradingAccountInput.getUserId())).thenReturn(db_user);
        Mockito.when(walletAccountService.getByUserId(createTradingAccountInput.getUserId())).thenReturn(db_walletAccount);

        CreateTradingAccountPayload expectedResult = tradingAccountService.createTradingAccount(createTradingAccountInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTradingAccountInput.getUserId());
        Mockito.verify(walletAccountService).getByUserId(createTradingAccountInput.getUserId());
    }

    @DisplayName("should Return Null when User IsExist And IsDeleted False And UserStatus Is Approved And WalletAccount Is Exist And WalletAccountStatus Not Approved From CreateTradingAccountInput")
    @Tag("createTradingAccount")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndUserStatusIsApprovedAndWalletAccountIsExistAndWalletAccountStatusNotApprovedFromCreateTradingAccountInput(){
        CreateTradingAccountInput createTradingAccountInput = new CreateTradingAccountInput("test_userId","test_apiId", Currency.TEST,100, TradingAccountClassification.TEST, TradingAccountType.TEST,new Date());
        User db_user = new User("test_userId","test_email","test_password","test_name","test_surname","test_phone","test_representativeEmployeeId", UserStatus.APPROVED,false,new Date(),new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletId","test_userId",Currency.TEST,0, WalletAccountStatus.WAITING,false,new Date(),new Date());

        Mockito.when(customUserService.findByUserId(createTradingAccountInput.getUserId())).thenReturn(db_user);
        Mockito.when(walletAccountService.getByUserId(createTradingAccountInput.getUserId())).thenReturn(db_walletAccount);

        CreateTradingAccountPayload expectedResult = tradingAccountService.createTradingAccount(createTradingAccountInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTradingAccountInput.getUserId());
        Mockito.verify(walletAccountService).getByUserId(createTradingAccountInput.getUserId());
    }

    @DisplayName("should Return Null when User Is Exist And IsDeleted False And UserStatus Is Approved And WalletAccount Is Exist And WalletAccount Status Is Approved And WalletAccount IsDeleted True From CreateTradingAccountInput")
    @Tag("createTradingAccount")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndUserStatusIsApprovedAndWalletAccountIsExistAndWalletAccountStatusIsApprovedAndWalletAccountIsDeletedTrueFromCreateTradingAccountInput(){
        CreateTradingAccountInput createTradingAccountInput = new CreateTradingAccountInput("test_userId","test_apiId", Currency.TEST,100, TradingAccountClassification.TEST, TradingAccountType.TEST,new Date());
        User db_user = new User("test_userId","test_email","test_password","test_name","test_surname","test_phone","test_representativeEmployeeId", UserStatus.APPROVED,false,new Date(),new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletId","test_userId",Currency.TEST,0, WalletAccountStatus.APPROVED,true,new Date(),new Date());

        Mockito.when(customUserService.findByUserId(createTradingAccountInput.getUserId())).thenReturn(db_user);
        Mockito.when(walletAccountService.getByUserId(createTradingAccountInput.getUserId())).thenReturn(db_walletAccount);

        CreateTradingAccountPayload expectedResult = tradingAccountService.createTradingAccount(createTradingAccountInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTradingAccountInput.getUserId());
        Mockito.verify(walletAccountService).getByUserId(createTradingAccountInput.getUserId());
    }

    @DisplayName("should Return DeleteTradingAccountPayload Status True when TradingAccount Is Exist And TradingAccount IsDeleted False FromDeleteTradingAccountInput")
    @Tag("deleteTradingAccount")
    @Test
    void shouldReturnDeleteTradingAccountPayloadStatusTrue_whenTradingAccountIsExistAndTradingAccountIsDeletedFalseFromDeleteTradingAccountInput(){
        DeleteTradingAccountInput deleteTradingAccountInput = new DeleteTradingAccountInput("test_tradingAccountId",new Date());
        TradingAccount tradingAccount = new TradingAccount("test_tradingAccountId","test_userId","test_apiId","test_walletAccountId",Currency.TEST,0,100,TradingAccountClassification.TEST,TradingAccountType.TEST,TradingAccountStatus.WAITING,false,new Date(),deleteTradingAccountInput.getDate());

        Mockito.when(tradingAccountRepository.findById(deleteTradingAccountInput.getId())).thenReturn(Optional.of(tradingAccount));
        Mockito.when(tradingAccountRepository.save(tradingAccount)).thenReturn(null);

        DeleteTradingAccountPayload expectedResult = tradingAccountService.deleteTradingAccount(deleteTradingAccountInput);

        assertTrue(expectedResult.isStatus());

        Mockito.verify(tradingAccountRepository).findById(deleteTradingAccountInput.getId());
        Mockito.verify(tradingAccountRepository).save(tradingAccount);

    }

    @DisplayName("should Return DeleteTradingAccountPayload Status False when TradingAccount Does Not Exist From DeleteTradingAccountInput")
    @Tag("deleteTradingAccount")
    @Test
    void shouldReturnDeleteTradingAccountPayloadStatusFalse_whenTradingAccountDoesNotExistFromDeleteTradingAccountInput(){
        DeleteTradingAccountInput deleteTradingAccountInput = new DeleteTradingAccountInput("test_tradingAccountId",new Date());

        Mockito.when(tradingAccountRepository.findById(deleteTradingAccountInput.getId())).thenReturn(Optional.empty());

        DeleteTradingAccountPayload expectedResult = tradingAccountService.deleteTradingAccount(deleteTradingAccountInput);

        assertFalse(expectedResult.isStatus());

        Mockito.verify(tradingAccountRepository).findById(deleteTradingAccountInput.getId());

    }

    @DisplayName("should Return DeleteTradingAccountPayload Status False when TradingAccount IsExist And TradingAccount IsDeleted True From DeleteTradingAccountInput")
    @Tag("deleteTradingAccount")
    @Test
    void shouldReturnDeleteTradingAccountPayloadStatusFalse_whenTradingAccountIsExistAndTradingAccountIsDeletedTrueFromDeleteTradingAccountInput(){
        DeleteTradingAccountInput deleteTradingAccountInput = new DeleteTradingAccountInput("test_tradingAccountId",new Date());
        TradingAccount tradingAccount = new TradingAccount("test_tradingAccountId","test_userId","test_apiId","test_walletAccountId",Currency.TEST,0,100,TradingAccountClassification.TEST,TradingAccountType.TEST,TradingAccountStatus.WAITING,true,new Date(),deleteTradingAccountInput.getDate());

        Mockito.when(tradingAccountRepository.findById(deleteTradingAccountInput.getId())).thenReturn(Optional.of(tradingAccount));

        DeleteTradingAccountPayload expectedResult = tradingAccountService.deleteTradingAccount(deleteTradingAccountInput);

        assertFalse(expectedResult.isStatus());

        Mockito.verify(tradingAccountRepository).findById(deleteTradingAccountInput.getId());
    }

    @DisplayName("should Return TradingAccountPayload when TradingAccount IsExist And IsDeleted False From GetTradingAccountByIdInput")
    @Tag("getTradingAccountById")
    @Test
    void shouldReturnTradingAccountPayload_whenTradingAccountIsExistAndIsDeletedFalseFromGetTradingAccountByIdInput(){
        GetTradingAccountByIdInput getTradingAccountByIdInput = new GetTradingAccountByIdInput("test_tradingAccountId");
        TradingAccount tradingAccount = new TradingAccount("test_tradingAccountId","test_userId","test_apiId","test_walletAccountId",Currency.TEST,0,100,TradingAccountClassification.TEST,TradingAccountType.TEST,TradingAccountStatus.WAITING,false,new Date(),new Date());
        TradingAccountPayload tradingAccountPayload = new TradingAccountPayload("test_tradingAccountId","test_userId","test_apiId","test_walletAccountId",Currency.TEST,0,100,TradingAccountClassification.TEST,TradingAccountType.TEST,TradingAccountStatus.WAITING,tradingAccount.getCreateDate());

        Mockito.when(tradingAccountRepository.findById(getTradingAccountByIdInput.getId())).thenReturn(Optional.of(tradingAccount));
        Mockito.when(mapperService.convertToTradingAccountPayload(tradingAccount)).thenReturn(tradingAccountPayload);

        TradingAccountPayload expectedResult = tradingAccountService.getTradingAccountById(getTradingAccountByIdInput);

        assertSame(expectedResult,tradingAccountPayload);

        Mockito.verify(tradingAccountRepository).findById(getTradingAccountByIdInput.getId());
        Mockito.verify(mapperService).convertToTradingAccountPayload(tradingAccount);

    }

    @DisplayName("should Return Null when TradingAccount Does Not Exist From GetTradingAccountByIdInput")
    @Tag("getTradingAccountById")
    @Test
    void shouldReturnNull_whenTradingAccountDoesNotExistFromGetTradingAccountByIdInput(){
        GetTradingAccountByIdInput getTradingAccountByIdInput = new GetTradingAccountByIdInput("test_tradingAccountId");

        Mockito.when(tradingAccountRepository.findById(getTradingAccountByIdInput.getId())).thenReturn(Optional.empty());

        TradingAccountPayload expectedResult = tradingAccountService.getTradingAccountById(getTradingAccountByIdInput);

        assertNull(expectedResult);

        Mockito.verify(tradingAccountRepository).findById(getTradingAccountByIdInput.getId());

    }

    @DisplayName("should Return Null when TradingAccount Is Exist And IsDeleted True From GetTradingAccountByIdInput")
    @Tag("getTradingAccountById")
    @Test
    void shouldReturnNull_whenTradingAccountIsExistAndIsDeletedTrueFromGetTradingAccountByIdInput(){
        GetTradingAccountByIdInput getTradingAccountByIdInput = new GetTradingAccountByIdInput("test_tradingAccountId");
        TradingAccount tradingAccount = new TradingAccount("test_tradingAccountId","test_userId","test_apiId","test_walletAccountId",Currency.TEST,0,100,TradingAccountClassification.TEST,TradingAccountType.TEST,TradingAccountStatus.WAITING,true,new Date(),new Date());

        Mockito.when(tradingAccountRepository.findById(getTradingAccountByIdInput.getId())).thenReturn(Optional.of(tradingAccount));

        TradingAccountPayload expectedResult = tradingAccountService.getTradingAccountById(getTradingAccountByIdInput);

        assertNull(expectedResult);

        Mockito.verify(tradingAccountRepository).findById(getTradingAccountByIdInput.getId());

    }

    @DisplayName("should Return TradingAccount when TradingAccount Is Exist And IsDeleted False From StringId")
    @Tag("findById")
    @Test
    void shouldReturnTradingAccount_whenTradingAccountIsExistAndIsDeletedFalseFromStringId(){
        TradingAccount tradingAccount = new TradingAccount("test_tradingAccountId","test_userId","test_apiId","test_walletAccountId",Currency.TEST,0,100,TradingAccountClassification.TEST,TradingAccountType.TEST,TradingAccountStatus.WAITING,false,new Date(),new Date());

        Mockito.when(tradingAccountRepository.findById("test_id")).thenReturn(Optional.of(tradingAccount));

        TradingAccount expectedResult = tradingAccountService.findById("test_id");

        assertSame(expectedResult,tradingAccount);

        Mockito.verify(tradingAccountRepository).findById("test_id");

    }

    @DisplayName("should Return Null when TradingAccount Is Exist And IsDeleted True From StringId")
    @Tag("findById")
    @Test
    void shouldReturnNull_whenTradingAccountIsExistAndIsDeletedTrueFromStringId(){
        TradingAccount tradingAccount = new TradingAccount("test_tradingAccountId","test_userId","test_apiId","test_walletAccountId",Currency.TEST,0,100,TradingAccountClassification.TEST,TradingAccountType.TEST,TradingAccountStatus.WAITING,true,new Date(),new Date());

        Mockito.when(tradingAccountRepository.findById("test_id")).thenReturn(Optional.of(tradingAccount));

        TradingAccount expectedResult = tradingAccountService.findById("test_id");

        assertNull(expectedResult);

        Mockito.verify(tradingAccountRepository).findById("test_id");
    }

    @DisplayName("should Return Null when TradingAccount Does Not Exist From String Id")
    @Tag("findById")
    @Test
    void shouldReturnNull_whenTradingAccountDoesNotExistFromStringId(){
        Mockito.when(tradingAccountRepository.findById("test_id")).thenReturn(Optional.empty());

        TradingAccount expectedResult = tradingAccountService.findById("test_id");

        assertNull(expectedResult);

        Mockito.verify(tradingAccountRepository).findById("test_id");
    }

    @DisplayName("should Return Page TradingAccountPayload when UserId Is Exist And IsDeleted False From GetAllTradingAccountByUserIdInput")
    @Tag("getAllTradingAccountsByUserId")
    @Test
    void shouldReturnPageTradingAccountPayload_whenUserIdIsExistAndIsDeletedFalseFromGetAllTradingAccountByUserIdInput(){
        GetAllTradingAccountsByUserIdInput getAllTradingAccountsByUserIdInput = new GetAllTradingAccountsByUserIdInput("test_userId",new PaginationInput(0,10,"id",SortBy.ASC));
        TradingAccount tradingAccount = new TradingAccount("test_tradingAccountId","test_userId","test_apiId","test_walletAccountId",Currency.TEST,0,100,TradingAccountClassification.TEST,TradingAccountType.TEST,TradingAccountStatus.WAITING,false,new Date(),new Date());
        TradingAccountPayload tradingAccountPayload = new TradingAccountPayload("test_tradingAccountId","test_userId","test_apiId","test_walletAccountId",Currency.TEST,0,100,TradingAccountClassification.TEST,TradingAccountType.TEST,TradingAccountStatus.WAITING,tradingAccount.getCreateDate());

        List<TradingAccount> tradingAccountList = new ArrayList<>();
        tradingAccountList.add(tradingAccount);
        Page<TradingAccount> tradingAccountPage = new PageImpl<>(tradingAccountList);

        Pageable pageable = PageRequest.of(getAllTradingAccountsByUserIdInput.getPaginationInput().getPage(),
                getAllTradingAccountsByUserIdInput.getPaginationInput().getSize(),
                Sort.by(Sort.Direction.valueOf(getAllTradingAccountsByUserIdInput.getPaginationInput().getSortBy().toString()),
                        getAllTradingAccountsByUserIdInput.getPaginationInput().getFieldName()));

        Mockito.when(tradingAccountRepository.findByUserIdAndIsDeletedFalse(getAllTradingAccountsByUserIdInput.getUserId(), pageable)).thenReturn(tradingAccountPage);
        Mockito.when(mapperService.convertToTradingAccountPayload(tradingAccount)).thenReturn(tradingAccountPayload);

        Page<TradingAccountPayload> expectedResult = tradingAccountService.getAllTradingAccountsByUserId(getAllTradingAccountsByUserIdInput);

        assertEquals(1,expectedResult.getContent().size());

        Mockito.verify(tradingAccountRepository).findByUserIdAndIsDeletedFalse(getAllTradingAccountsByUserIdInput.getUserId(), pageable);
        Mockito.verify(mapperService).convertToTradingAccountPayload(tradingAccount);

    }

    @AfterEach
    void tearDown() {
    }

}
