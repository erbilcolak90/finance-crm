package com.financecrm.webportal;

import com.financecrm.webportal.entities.BankAccount;
import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.enums.*;
import com.financecrm.webportal.input.PaginationInput;
import com.financecrm.webportal.input.bankaccount.CreateBankAccountInput;
import com.financecrm.webportal.input.bankaccount.DeleteBankAccountInput;
import com.financecrm.webportal.input.bankaccount.GetAllBankAccountsByUserIdInput;
import com.financecrm.webportal.input.bankaccount.GetBankAccountByIdInput;
import com.financecrm.webportal.payload.bankaccount.BankAccountPayload;
import com.financecrm.webportal.payload.bankaccount.CreateBankAccountPayload;
import com.financecrm.webportal.payload.bankaccount.DeleteBankAccountPayload;
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
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    @Tag("createBankAccount")
    @Test
    void shouldReturnCreateBankAccountPayload_whenUserIdIsExistAndIbanDoesNotExistFromCreateBankAccountInput() {
        // 2. adım : test verilerinin hazırlanması

        CreateBankAccountInput createBankAccountInput = new CreateBankAccountInput("test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", new Date(), new Date());

        BankAccount dbBankAccount = null;
        BankAccount bankAccount = new BankAccount("test_id", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.WAITING, false, createBankAccountInput.getCreateDate(), createBankAccountInput.getUpdateDate());
        BankAccount beforeSavedBankAccount = new BankAccount(null, "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.WAITING, false, createBankAccountInput.getCreateDate(), createBankAccountInput.getUpdateDate());
        CreateBankAccountPayload result = new CreateBankAccountPayload("test_id", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.WAITING, createBankAccountInput.getCreateDate());
        User db_user = new User("test_userId","test_email","test_password","test_name","test_surname","test_phone","test_representativeEmployeeId", UserStatus.TEST,false,new Date(),new Date());

        //3. adım : bağımlı servicelerin davranışlarının belirlenmesi
        Mockito.when(bankAccountRepository.findByIban(createBankAccountInput.getIban())).thenReturn(null);
        Mockito.when(customUserService.findByUserId(createBankAccountInput.getUserId())).thenReturn(db_user);
        Mockito.when(customUserService.findByUserId(createBankAccountInput.getUserId())).thenReturn(new User());
        Mockito.when(bankAccountRepository.findByIban("test_iban")).thenReturn(Optional.ofNullable(dbBankAccount));
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

    @DisplayName("shold Return Null when BankAccount Is Exist And User Does Not Exist From CreateBankAccountInput")
    @Tag("createBankAccount")
    @Test
    // 1. adım : test isminin yazılması
    void sholdReturnNull_whenBankAccountIsExistAndUserDoesNotExistFromCreateBankAccountInput(){
        // 2. adım: değişkenlerin belirlenmesi

        CreateBankAccountInput createBankAccountInput = new CreateBankAccountInput("test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", new Date(), new Date());
        Optional<BankAccount> bankAccount = Optional.of(new BankAccount("test_id", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.WAITING, false, createBankAccountInput.getCreateDate(), createBankAccountInput.getUpdateDate()));

        // 3. adım : bağımlı değişkenlerin davranışları
        Mockito.when(bankAccountRepository.findByIban("test_iban")).thenReturn(bankAccount);
        Mockito.when(customUserService.findByUserId(createBankAccountInput.getUserId())).thenReturn(null);

        // 4. adım : test metodunun çalıştırılması
        CreateBankAccountPayload expectedResult = bankAccountService.createBankAccount(createBankAccountInput);

        // 5. adım: test sonuçlarının karşılaştırılması
        assertNull(expectedResult);

        // 6. adım : bağımlı servislerin çalışmasının kontrolü
        Mockito.verify(bankAccountRepository).findByIban(anyString());
        Mockito.verify(customUserService).findByUserId(createBankAccountInput.getUserId());
    }

    // 1. adım :Test isminin yazılması
    @DisplayName("should Return DeleteBankAccountPayload when BankAccount IsExist From DeleteBankAccountInput")
    @Tag("deleteBankAccount")
    @Test
    void shouldReturnDeleteBankAccountPayload_whenBankAccountIsExistFromDeleteBankAccountInput(){
        // 2. adım :  test verilerinin hazırlanması
        DeleteBankAccountInput deleteBankAccountInput = new DeleteBankAccountInput("test_id");
        BankAccount db_bankAccount = new BankAccount("test_id", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.WAITING, false, new Date(), new Date());
        DeleteBankAccountPayload result = new DeleteBankAccountPayload(true);

        // 3. adım : bağımlı değişkenlerin davranışlarının belirlenmesi
        Mockito.when(bankAccountRepository.findById(deleteBankAccountInput.getId())).thenReturn(Optional.of(db_bankAccount));
        Mockito.when(bankAccountRepository.save(db_bankAccount)).thenReturn(null);

        // 4. adım : test metodunun çalıştırılması
        DeleteBankAccountPayload expectedResult = bankAccountService.deleteBankAccount(deleteBankAccountInput);

        // 5. adım : test sonuçlarının karşılaştırılması

        assertEquals(expectedResult.isStatus(),result.isStatus());

        // 6. adım : bağımlı servislerinin çalıştırıldığının kontrol edilmesi
        Mockito.verify(bankAccountRepository).findById(deleteBankAccountInput.getId());
        Mockito.verify(bankAccountRepository).save(db_bankAccount);
    }

    // 1. adım :Test isminin yazılması
    @DisplayName("should Return DeleteBankAccountPayload when BankAccount Does Not Exist From DeleteBankAccountInput")
    @Tag("deleteBankAccount")
    @Test
    void shouldReturnDeleteBankAccountPayload_whenBankAccountDoesNotExistFromDeleteBankAccountInput(){
        // 2. adım :  test verilerinin hazırlanması
        DeleteBankAccountInput deleteBankAccountInput = new DeleteBankAccountInput("test_id");
        DeleteBankAccountPayload result = new DeleteBankAccountPayload(false);

        // 3. adım : bağımlı değişkenlerin davranışlarının belirlenmesi
        Mockito.when(bankAccountRepository.findById(deleteBankAccountInput.getId())).thenReturn(Optional.empty());

        // 4. adım : test metodunun çalıştırılması
        DeleteBankAccountPayload expectedResult = bankAccountService.deleteBankAccount(deleteBankAccountInput);

        // 5. adım : test sonuçlarının karşılaştırılması
        assertEquals(expectedResult.isStatus(),result.isStatus());

        // 6. adım : bağımlı servislerinin çalıştırıldığının kontrol edilmesi
        Mockito.verify(bankAccountRepository).findById(deleteBankAccountInput.getId());
    }

    // 1.adım = Test isminin yazılması
    @DisplayName("should Return BankAccountPayload when Id From GetBankAccountByIdInput IsExist")
    @Tag("getBankAccountById")
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

    // 1.adım = Test isminin yazılması
    @DisplayName("shold return null when Id From GetBankAccountByIdInput Does Not Exist")
    @Tag("getBankAccountById")
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

    // 1. adım : test isminin yazılması
    @DisplayName("should Return PageBankAccountPayload when User Is Exist From GetAllBankAccountsByUserIdInput")
    @Tag("getAllBankAccountsByUserId")
    @Test
    void shouldReturnPageBankAccountPayload_whenUserIsExistFromGetAllBankAccountsByUserIdInput(){
    // 2. adım : test verilerinin hazırlanması
        GetAllBankAccountsByUserIdInput getAllBankAccountsByUserIdInput = new GetAllBankAccountsByUserIdInput("test_userId",new PaginationInput(0,10,"test_fieldName", SortBy.ASC));
        User db_user = new User("test_userId","test_email","test_password","test_name","test_surname","test_phone","test_representativeEmployeeId", UserStatus.TEST,false,new Date(),new Date());
        BankAccount db_bankAccount = new BankAccount("test_id", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.WAITING, false, new Date(), new Date());
        List<BankAccount> bankAccountList = new ArrayList<>();
        bankAccountList.add(db_bankAccount);
        Page<BankAccount> bankAccountPage = new PageImpl<>(bankAccountList);

        Pageable pageable = PageRequest.of(getAllBankAccountsByUserIdInput.getPaginationInput().getPage(),
                getAllBankAccountsByUserIdInput.getPaginationInput().getSize(),
                Sort.by(Sort.Direction.valueOf(getAllBankAccountsByUserIdInput.getPaginationInput().getSortBy().toString()),
                        getAllBankAccountsByUserIdInput.getPaginationInput().getFieldName()));


        // 3. adım : bağımlı değişkenlerin davranışlarının belirlenmesi
        Mockito.when(customUserService.findByUserId("test_userId")).thenReturn(db_user);
        Mockito.when(bankAccountRepository.findByUserIdAndIsDeletedFalse("test_userId",pageable)).thenReturn(bankAccountPage);
        Mockito.when(mapperService.convertToBankAccountPayload(db_bankAccount)).thenReturn(new BankAccountPayload());

        // 4. adım : test metodunun çalıştırılması
        Page<BankAccountPayload> expectedResult = bankAccountService.getAllBankAccountsByUserId(getAllBankAccountsByUserIdInput);

        // 5. adım : test sonuçlarının karşılaştırılması
        assertEquals(1,expectedResult.getContent().size());

        // 6. adım : bağımlı değişkenlerin çalıştığının doğrulanması
        Mockito.verify(customUserService).findByUserId("test_userId");
        Mockito.verify(bankAccountRepository).findByUserIdAndIsDeletedFalse("test_userId",pageable);
        Mockito.verify(mapperService).convertToBankAccountPayload(db_bankAccount);

    }

    // 1. adım : test adının yazılması
    @DisplayName("should Return null when User Does Not Exist From GetAllBankAccountsByUserIdInput")
    @Tag("getAllBankAccountsByUserId")
    @Test
    void shouldReturnNull_whenUserDoesNotExistOrUserIsDeletedTrueFromGetAllBankAccountsByUserIdInput(){
        // 2. adım : test verilerinin hazırlanması
        User db_user = null;
        GetAllBankAccountsByUserIdInput getAllBankAccountsByUserIdInput = new GetAllBankAccountsByUserIdInput("test_userId",new PaginationInput(0,10,"test_fieldName", SortBy.ASC));
        // 3. adım : bağımlı değişkenlerin davranışlarının belirlenmesi.
        Mockito.when(customUserService.findByUserId(getAllBankAccountsByUserIdInput.getUserId())).thenReturn(db_user);
        // 4. adım : test metodunun çalıştırılması
        Page<BankAccountPayload> expectedResult = bankAccountService.getAllBankAccountsByUserId(getAllBankAccountsByUserIdInput);
        // 5. adım : test sonuçlarının karşılaştırılması
        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(getAllBankAccountsByUserIdInput.getUserId());
    }


    @DisplayName("should Return BankAccount when Id Is Exist")
    @Tag("findById")
    @Test
    void shouldReturnBankAccount_whenIdIsExist(){
        String id = "test_id";
        BankAccount db_bankAccount = new BankAccount("test_id", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.WAITING, false, new Date(), new Date());

        Mockito.when(bankAccountRepository.findById(id)).thenReturn(Optional.of(db_bankAccount));

        BankAccount expectedResult = bankAccountService.findById(id);

        assertEquals(expectedResult.getId(),db_bankAccount.getId());

        Mockito.verify(bankAccountRepository).findById(id);

    }

    @AfterEach
    void tearDown() {
    }


}
