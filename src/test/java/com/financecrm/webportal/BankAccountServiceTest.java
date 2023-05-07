package com.financecrm.webportal;

import com.financecrm.webportal.entities.BankAccount;
import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.enums.BankAccountStatus;
import com.financecrm.webportal.enums.BankName;
import com.financecrm.webportal.enums.Currency;
import com.financecrm.webportal.input.bankaccount.CreateBankAccountInput;
import com.financecrm.webportal.input.bankaccount.GetBankAccountByIdInput;
import com.financecrm.webportal.payload.bankaccount.BankAccountPayload;
import com.financecrm.webportal.payload.bankaccount.CreateBankAccountPayload;
import com.financecrm.webportal.repositories.BankAccountRepository;
import com.financecrm.webportal.services.BankAccountService;
import com.financecrm.webportal.services.CustomUserService;
import com.financecrm.webportal.services.MapperService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {

    @InjectMocks
    private BankAccountService bankAccountService;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private MapperService mapperService;
    @Mock
    private CustomUserService customUserService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    // 1. adım : test isminin yazılması
    @DisplayName("should Return CreateBankAccountPayload when UserId Is Exist And Iban Does Not Exist From CreateBankAccountInput")
    @Test
    void shouldReturnCreateBankAccountPayload_whenUserIdIsExistAndIbanDoesNotExistFromCreateBankAccountInput() {
        // 2. adım : test verilerinin hazırlanması

        CreateBankAccountInput createBankAccountInput = new CreateBankAccountInput("test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", new Date(), new Date());

        BankAccount dbBankAccount = null;
        BankAccount bankAccount = new BankAccount("test_id", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.WAITING, false, createBankAccountInput.getCreateDate(), createBankAccountInput.getUpdateDate());
        BankAccount beforeSavedBankAccount = new BankAccount(null, "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.WAITING, false, createBankAccountInput.getCreateDate(), createBankAccountInput.getUpdateDate());
        CreateBankAccountPayload result = new CreateBankAccountPayload("test_id", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.WAITING, createBankAccountInput.getCreateDate());

        //3. adım : bağımlı servicelerin davranışlarının belirlenmesi
        Mockito.when(customUserService.findByUserId(createBankAccountInput.getUserId())).thenReturn(new User());
        Mockito.when(bankAccountRepository.findByIban("test_iban")).thenReturn(dbBankAccount);
        Mockito.when(bankAccountRepository.save(beforeSavedBankAccount)).thenReturn(bankAccount);
        Mockito.when(mapperService.convertToCreateBankAccountPayload(bankAccount)).thenReturn(result);

        // 4. adım : test edilecek metodun çalıştırılması
        CreateBankAccountPayload expectedResult = bankAccountService.createBankAccount(createBankAccountInput);

        // 5. adım : test sonuçlarının karşılaştırılması
        assertEquals(expectedResult.getIban(),result.getIban());
        assertEquals(expectedResult.getUserId(),result.getUserId());

        // 6. adım : bağımlı servislerin çalıştırılmasının kontrol edilmesi.
        Mockito.verify(customUserService).findByUserId(createBankAccountInput.getUserId());
        Mockito.verify(bankAccountRepository).findByIban(anyString());
        Mockito.verify(bankAccountRepository).save(beforeSavedBankAccount);
        Mockito.verify(mapperService).convertToCreateBankAccountPayload(bankAccount);
    }


    // getBankAccountById
    // 1.adım = Test isminin yazılması
    @DisplayName("should Return BankAccountPayload when Id From GetBankAccountByIdInput IsExist")
    @Test
    void shouldReturnBankAccountPayload_whenIdFromGetBankAccountByIdInputIsExist() {

        //2. adım : Test verilerinin hazırlanması
        GetBankAccountByIdInput getBankAccountByIdInput = new GetBankAccountByIdInput("testId");
        BankAccount bankAccount = new BankAccount(getBankAccountByIdInput.getId(), "test_UserId", "test_alias", BankName.AK_BANK, "test_iban", Currency.USD, "test_swiftCode", BankAccountStatus.WAITING, false, new Date(), new Date());

        BankAccountPayload result = new BankAccountPayload(bankAccount.getId(), bankAccount.getUserId(), bankAccount.getAlias(), bankAccount.getBankName(), bankAccount.getIban(), bankAccount.getCurrency(), bankAccount.getSwiftCode(), bankAccount.getStatus(), bankAccount.getCreateDate());

        // 3. adım : bağımlı servicelerin davranışlarının belirlenmesi
        Mockito.when(bankAccountRepository.findById(getBankAccountByIdInput.getId())).thenReturn(Optional.of(bankAccount));
        Mockito.when(mapperService.convertToBankAccountPayload(bankAccount)).thenReturn(result);

        // 4. adım : test edilecek metodun çalıştırılması
        BankAccountPayload expectedResultBankAccountPayload = bankAccountService.getBankAccountById(getBankAccountByIdInput);

        // 5. adım : test sonuçlarının karşılaştırılması
        assertEquals(expectedResultBankAccountPayload, result);

        // 6. adım : bağımlı servislerin çalıştırılmasının kontrol edilmesi.
        Mockito.verify(bankAccountRepository).findById(getBankAccountByIdInput.getId());
        Mockito.verify(mapperService).convertToBankAccountPayload(bankAccount);

    }

    // getBankAccountById
    // 1.adım = Test isminin yazılması
    @DisplayName("shold return null when Id From GetBankAccountByIdInput Does Not Exist")
    @Test
    void shouldReturnNull_whenIdFromGetBankAccountByIdInputDoesNotExist() {

        //2. adım : Test verilerinin hazırlanması
        GetBankAccountByIdInput getBankAccountByIdInput = new GetBankAccountByIdInput("test_id");
        BankAccount bankAccount = null;
        BankAccountPayload bankAccountPayload = null;

        // 3. adım : bağımlı servicelerin davranışlarının belirlenmesi
        Mockito.when(bankAccountRepository.findById(getBankAccountByIdInput.getId())).thenReturn(Optional.empty());
        Mockito.when(mapperService.convertToBankAccountPayload(bankAccount)).thenReturn(bankAccountPayload);

        // 4. adım : test edilecek metodun çalıştırılması
        BankAccountPayload expectedResult = bankAccountService.getBankAccountById(getBankAccountByIdInput);

        // 5. adım : test sonuçlarının karşılaştırılması
        assertNull(expectedResult);

        /*org.assertj.core.api.Assertions.assertThatThrownBy(()-> bankAccountRepository.findById(getBankAccountByIdInput.getId()))
                .isInstanceOf(null);*/

        // 6. adım : bağımlı servislerin çalıştırılmasının kontrol edilmesi.
        Mockito.verify(bankAccountRepository).findById(getBankAccountByIdInput.getId());
        Mockito.verify(mapperService).convertToBankAccountPayload(bankAccount);

    }

    @AfterEach
    void tearDown() {
    }


}
