package com.financecrm.webportal;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.entities.WalletAccount;
import com.financecrm.webportal.enums.Currency;
import com.financecrm.webportal.enums.WalletAccountStatus;
import com.financecrm.webportal.input.walletaccount.GetWalletAccountByUserIdInput;
import com.financecrm.webportal.payload.walletaccount.WalletAccountPayload;
import com.financecrm.webportal.repositories.WalletAccountRepository;
import com.financecrm.webportal.services.CustomUserService;
import com.financecrm.webportal.services.MapperService;
import com.financecrm.webportal.services.WalletAccountService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletAccountServiceTest {

    @InjectMocks
    private WalletAccountService walletAccountService;
    @Mock
    private WalletAccountRepository walletAccountRepository;
    @Mock
    private CustomUserService customUserService;
    @Mock
    private MapperService mapperService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("should Return WalletAccount when UserId Is Exist In WalletAccount User From String")
    @Tag("getUserId")
    @Test
    void shouldReturnWalletAccount_whenUserIdIsExistInWalletAccountUserFromString(){
        String userId = "test_userId";
        WalletAccount walletAccount = new WalletAccount();
        walletAccount.setUserId(userId);

        when(walletAccountRepository.findByUserId(userId)).thenReturn(Optional.of(walletAccount));

        WalletAccount expectedWalletAccount = walletAccountService.getByUserId(userId);

        assertEquals(expectedWalletAccount,walletAccount);

        verify(walletAccountRepository).findByUserId(userId);

    }
    @DisplayName("should Return Null when UserId Does Not Exist In WalletAccount User From String")
    @Tag("getUserId")
    @Test
    void shouldReturnNull_whenUserIdDoesNotExistInWalletAccountUserFromString(){
        String userId = "test_userId";

        when(walletAccountRepository.findByUserId(userId)).thenReturn(Optional.empty());

        WalletAccount expectedWalletAccount = walletAccountService.getByUserId(userId);

        assertNull(expectedWalletAccount);

        verify(walletAccountRepository).findByUserId(userId);

    }

    @DisplayName("should Return WalletAccount when Id Is Exist In WalletAccount From String")
    @Tag("findById")
    @Test
    void shouldReturnWalletAccount_whenIdIsExistInWalletAccountFromString(){
        String id = "test_id";
        WalletAccount walletAccount = new WalletAccount();
        walletAccount.setId(id);

        when(walletAccountRepository.findById(id)).thenReturn(Optional.of(walletAccount));

        WalletAccount expectedWalletAccount = walletAccountService.findById(id);

        assertEquals(expectedWalletAccount,walletAccount);

        verify(walletAccountRepository).findById(id);
    }

    @DisplayName("should Return Null when Id Does Not Exist In WalletAccount From String")
    @Tag("findById")
    @Test
    void shouldReturnNull_whenIdDoesNotExistInWalletAccountFromString(){
        String id = "test_id";

        when(walletAccountRepository.findById(id)).thenReturn(Optional.empty());

        WalletAccount expectedWalletAccount = walletAccountService.findById(id);

        assertNull(expectedWalletAccount);

        verify(walletAccountRepository).findById(id);

    }

    @DisplayName("testCreateWalletAccount")
    @Tag("createWalletAccount")
    @Test
    void testCreateWalletAccount(){
        String userId = "test_userId";
        User mockUser = new User();
        mockUser.setId(userId);
        WalletAccount mockWalletAccount = new WalletAccount();

        when(customUserService.findByUserId(userId)).thenReturn(mockUser);
        when(walletAccountRepository.save(any(WalletAccount.class))).thenReturn(mockWalletAccount);

        walletAccountService.createWalletAccount(userId);

        verify(customUserService).findByUserId(userId);
        ArgumentCaptor<WalletAccount> walletAccountCaptor = ArgumentCaptor.forClass(WalletAccount.class);
        verify(walletAccountRepository).save(walletAccountCaptor.capture());
        WalletAccount savedWalletAccount = walletAccountCaptor.getValue();
        assertEquals(0, savedWalletAccount.getBalance());
        assertEquals(Currency.USD, savedWalletAccount.getCurrency());
        assertEquals(WalletAccountStatus.WAITING, savedWalletAccount.getStatus());
        assertFalse(savedWalletAccount.isDeleted());
        assertNotNull(savedWalletAccount.getCreateDate());
        assertNotNull(savedWalletAccount.getUpdateDate());
        assertEquals(userId, savedWalletAccount.getUserId());

    }

    @DisplayName("should Return WalletAccountPayload when UserId Is Exist In WalletAccount From GetWalletAccountByUserIdInput")
    @Tag("getWalletAccountByUserId")
    @Test
    void shouldReturnWalletAccountPayload_whenUserIdIsExistInWalletAccountFromGetWalletAccountByUserIdInput(){
        GetWalletAccountByUserIdInput getWalletAccountByUserIdInput = new GetWalletAccountByUserIdInput("test_userId");
        WalletAccount walletAccount = new WalletAccount();
        walletAccount.setUserId(getWalletAccountByUserIdInput.getUserId());
        WalletAccountPayload walletAccountPayload = new WalletAccountPayload();
        walletAccountPayload.setUserId(walletAccountPayload.getUserId());

        when(walletAccountRepository.findByUserId(getWalletAccountByUserIdInput.getUserId())).thenReturn(Optional.of(walletAccount));
        when(mapperService.convertToWalletAccountPayload(walletAccount)).thenReturn(walletAccountPayload);

        WalletAccountPayload expectedWalletAccount = walletAccountService.getWalletAccountByUserId(getWalletAccountByUserIdInput);

        assertEquals(expectedWalletAccount,walletAccountPayload);

        verify(walletAccountRepository).findByUserId(getWalletAccountByUserIdInput.getUserId());
        verify(mapperService).convertToWalletAccountPayload(walletAccount);

    }

    @DisplayName("should Return Null when UserId Does Not Exist In WalletAccount From GetWalletAccountByUserIdInput")
    @Tag("getWalletAccountByUserId")
    @Test
    void shouldReturnNull_whenUserIdDoesNotExistInWalletAccountFromGetWalletAccountByUserIdInput(){
        GetWalletAccountByUserIdInput getWalletAccountByUserIdInput = new GetWalletAccountByUserIdInput("test_userId");

        when(walletAccountRepository.findByUserId(getWalletAccountByUserIdInput.getUserId())).thenReturn(Optional.empty());

        WalletAccountPayload expectedWalletAccount = walletAccountService.getWalletAccountByUserId(getWalletAccountByUserIdInput);

        assertNull(expectedWalletAccount);

        verify(walletAccountRepository).findByUserId(getWalletAccountByUserIdInput.getUserId());
    }

    @Test
    void testSave() {
        WalletAccount db_walletAccount = new WalletAccount();
        db_walletAccount.setUserId("testUser");
        db_walletAccount.setBalance(100);
        db_walletAccount.setCurrency(Currency.TEST);
        db_walletAccount.setStatus(WalletAccountStatus.APPROVED);
        db_walletAccount.setDeleted(false);
        db_walletAccount.setCreateDate(new Date());
        db_walletAccount.setUpdateDate(new Date());

        walletAccountService.save(db_walletAccount);

        verify(walletAccountRepository, times(1)).save(db_walletAccount);
    }


    @AfterEach
    void tearDown() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }
}
