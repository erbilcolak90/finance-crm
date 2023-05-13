package com.financecrm.webportal;

import com.financecrm.webportal.entities.*;
import com.financecrm.webportal.enums.*;
import com.financecrm.webportal.event.TransferEvent;
import com.financecrm.webportal.input.PaginationInput;
import com.financecrm.webportal.input.transfer.CreateTransferInput;
import com.financecrm.webportal.input.transfer.DeleteTransferInput;
import com.financecrm.webportal.input.transfer.GetAllTransfersByUserIdInput;
import com.financecrm.webportal.input.transfer.GetTransferByIdInput;
import com.financecrm.webportal.payload.transfer.CreateTransferPayload;
import com.financecrm.webportal.payload.transfer.DeleteTransferPayload;
import com.financecrm.webportal.payload.transfer.TransferPayload;
import com.financecrm.webportal.repositories.TransferRepository;
import com.financecrm.webportal.services.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @InjectMocks
    private TransferService transferService;
    @Mock
    private TransferRepository transferRepository;
    @Mock
    private WalletAccountService walletAccountService;
    @Mock
    private MapperService mapperService;
    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private TradingAccountService tradingAccountService;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private CustomUserService customUserService;


    @BeforeEach
    void setUp() {
        TransferService transferService = Mockito.mock(TransferService.class);
        MockitoAnnotations.openMocks(this);

    }

    @DisplayName("should Return TransferPayload when Transfer Is Exist And IsDeleted False From GetTransferByIdInput")
    @Tag("getTransferById")
    @Test
    void shouldReturnTransferPayload_whenTransferIsExistAndIsDeletedFalseFromGetTransferByIdInput() {
        GetTransferByIdInput getTransferByIdInput = new GetTransferByIdInput("test_transferId");
        Transfer transfer = new Transfer("test_transferId", "test_userId", "test_fromAccountId", "test_toAccountId", 0, TransferStatus.TEST, TransferType.TEST, false, new Date());
        TransferPayload transferPayload = new TransferPayload("test_transferId", "test_fromAccountId", "test_toAccountId", 0, TransferStatus.TEST, TransferType.TEST, transfer.getCreateDate());

        Mockito.when(transferRepository.findById(getTransferByIdInput.getId())).thenReturn(Optional.of(transfer));
        Mockito.when(mapperService.convertToTransferPayload(transfer)).thenReturn(transferPayload);

        TransferPayload expectedResult = transferService.getTransferById(getTransferByIdInput);

        assertSame(expectedResult, transferPayload);

        Mockito.verify(transferRepository).findById(getTransferByIdInput.getId());
        Mockito.verify(mapperService).convertToTransferPayload(transfer);

    }

    @DisplayName("should Return Null when Transfer IsExist And IsDeleted True From GetTransferByIdInput")
    @Tag("getTransferById")
    @Test
    void shouldReturnNull_whenTransferIsExistAndIsDeletedTrueFromGetTransferByIdInput() {
        GetTransferByIdInput getTransferByIdInput = new GetTransferByIdInput("test_transferId");
        Transfer transfer = new Transfer("test_transferId", "test_userId", "test_fromAccountId", "test_toAccountId", 0, TransferStatus.TEST, TransferType.TEST, true, new Date());

        Mockito.when(transferRepository.findById(getTransferByIdInput.getId())).thenReturn(Optional.of(transfer));

        TransferPayload expectedResult = transferService.getTransferById(getTransferByIdInput);

        assertNull(expectedResult);

        Mockito.verify(transferRepository).findById(getTransferByIdInput.getId());
    }

    @DisplayName("should Return Null when Transfer Does Not Exist From GetTransferByIdInput")
    @Tag("getTransferById")
    @Test
    void shouldReturnNull_whenTransferDoesNotExistFromGetTransferByIdInput() {
        GetTransferByIdInput getTransferByIdInput = new GetTransferByIdInput("test_transferId");

        Mockito.when(transferRepository.findById(getTransferByIdInput.getId())).thenReturn(Optional.empty());

        TransferPayload expectedResult = transferService.getTransferById(getTransferByIdInput);

        assertNull(expectedResult);

        Mockito.verify(transferRepository).findById(getTransferByIdInput.getId());
    }

    @DisplayName("should Return Page TransferPayload when UserId IsExist And IsDeleted False From GetAllTransfersByUserIdInput")
    @Tag("getAllTransfersByUserId")
    @Test
    void shouldReturnPageTransferPayload_whenUserIdIsExistAndIsDeletedFalseFromGetAllTransfersByUserIdInput() {
        GetAllTransfersByUserIdInput getAllTransfersByUserIdInput = new GetAllTransfersByUserIdInput("test_userId", new PaginationInput(0, 10, "test_transferId", SortBy.ASC));
        Transfer transfer = new Transfer("test_transferId", "test_userId", "test_fromAccountId", "test_toAccountId", 0, TransferStatus.TEST, TransferType.TEST, false, new Date());
        TransferPayload transferPayload = new TransferPayload("test_transferId", "test_fromAccountId", "test_toAccountId", 0, TransferStatus.TEST, TransferType.TEST, transfer.getCreateDate());

        List<Transfer> transferList = new ArrayList<>();
        transferList.add(transfer);
        Page<Transfer> transferPage = new PageImpl<>(transferList);

        Pageable pageable = PageRequest.of(getAllTransfersByUserIdInput.getPaginationInput().getPage(),
                getAllTransfersByUserIdInput.getPaginationInput().getSize(),
                Sort.by(Sort.Direction.valueOf(getAllTransfersByUserIdInput.getPaginationInput().getSortBy().toString()),
                        getAllTransfersByUserIdInput.getPaginationInput().getFieldName()));


        Mockito.when(transferRepository.findByUserIdAndIsDeletedFalse(getAllTransfersByUserIdInput.getUserId(), pageable)).thenReturn(transferPage);
        Mockito.when(mapperService.convertToTransferPayload(transfer)).thenReturn(transferPayload);

        Page<TransferPayload> expectedResult = transferService.getAllTransfersByUserId(getAllTransfersByUserIdInput);

        assertEquals(1, expectedResult.getContent().size());

        Mockito.verify(transferRepository).findByUserIdAndIsDeletedFalse(getAllTransfersByUserIdInput.getUserId(), pageable);
        Mockito.verify(mapperService).convertToTransferPayload(transfer);

    }

    @DisplayName("should Return Null when User Does Not Exist From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserDoesNotExistFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_fromnAccountId", "test_toAccountId", 0, TransferType.TEST, new Date());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(null);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());

    }

    @DisplayName("should Return Null when User Is Exist And Is Deleted True From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedTrueFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_fromnAccountId", "test_toAccountId", 0, TransferType.TEST, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, true, new Date(), new Date());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
    }

    @DisplayName("should Return Create TransferPayload And TransferType Equals Withdraw when User IsExist And IsDeleted False And TransferType Is Withdraw And FromAccountId Exist In WalletAccount And ToAccountId Exist In BankAccount And BankAccountId IsDeleted False And WalletAccountStatus Is Approved And WalletAccount Balance Equals Or Bigger Than Amount From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnCreateTransferPayloadAndTransferTypeEqualsWithdraw_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsWithdrawAndFromAccountIdExistInWalletAccountAndToAccountIdExistInBankAccountAndBankAccountIdIsDeletedFalseAndWalletAccountStatusIsApprovedAndWalletAccountBalanceEqualsOrBiggerThanAmountFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_fromAccountId", "test_toAccountId", 25, TransferType.WITHDRAW, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 100, WalletAccountStatus.APPROVED, false, new Date(), new Date());
        BankAccount db_bankAccount = new BankAccount("test_bankAccountId", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.APPROVED, false, new Date(), new Date());
        Transfer transfer = new Transfer(null, "test_userId", "test_fromAccountId", "test_toAccountId", 25, TransferStatus.WAITING, TransferType.WITHDRAW, false, createTransferInput.getDate());
        Transfer savedTransfer = new Transfer("test_transferId", "test_userId", "test_fromAccountId", "test_toAccountId", 25, TransferStatus.WAITING, TransferType.WITHDRAW, false, createTransferInput.getDate());
        CreateTransferPayload createTransferPayload = new CreateTransferPayload("test_transferId", "test_fromAccountId", "test_toAccountId", 25, TransferStatus.WAITING, TransferType.WITHDRAW, createTransferInput.getDate());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_walletAccount);
        Mockito.when(bankAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_bankAccount);
        Mockito.when(transferRepository.save(transfer)).thenReturn(savedTransfer);
        Mockito.when(mapperService.convertToCreateTransferPayload(savedTransfer)).thenReturn(createTransferPayload);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertSame(expectedResult, createTransferPayload);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getFromAccountId());
        Mockito.verify(bankAccountService).findById(createTransferInput.getToAccountId());
        Mockito.verify(transferRepository).save(transfer);
        Mockito.verify(mapperService).convertToCreateTransferPayload(savedTransfer);

    }

    @DisplayName("should Return Null when User IsExist And IsDeleted False And TransferType Is Withdraw And FromAccountId Does Not Exist In WalletAccount And ToAccountId Is Exist In BankAccount From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsWithdrawAndFromAccountIdDoesNotExistInWalletAccountAndToAccountIdIsExistInBankAccountFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_fromAccountId", "test_toAccountId", 25, TransferType.WITHDRAW, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 100, WalletAccountStatus.APPROVED, false, new Date(), new Date());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_walletAccount);
        Mockito.when(bankAccountService.findById(createTransferInput.getToAccountId())).thenReturn(null);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getFromAccountId());
        Mockito.verify(bankAccountService).findById(createTransferInput.getToAccountId());

    }

    @DisplayName("should Return Null when User IsExist And IsDeleted False And TransferType Is Withdraw And FromAccountId Does Not Exist In WalletAccount And ToAccountId Does Not Exist In BankAccount From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsWithdrawAndFromAccountIdDoesNotInWalletAccountAndToAccountIdDoesNotExistInBankAccountFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_fromAccountId", "test_toAccountId", 25, TransferType.WITHDRAW, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        BankAccount db_bankAccount = new BankAccount("test_bankAccountId", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.APPROVED, false, new Date(), new Date());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(null);
        Mockito.when(bankAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_bankAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getFromAccountId());
        Mockito.verify(bankAccountService).findById(createTransferInput.getToAccountId());

    }

    @DisplayName("should Return Null when User Is Exist And IsDeleted False And TransferType Is Withdraw And FromAccountId Is Exist In WalletAccount And ToAccountId Is Exist In BankAccount And BankAccount IsDeleted True From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsWithdrawAndFromAccountIdIsExistInWalletAccountAndToAccountIdIsExistInBankAccountAndBankAccountIsDeletedTrueFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_fromAccountId", "test_toAccountId", 25, TransferType.WITHDRAW, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 100, WalletAccountStatus.APPROVED, false, new Date(), new Date());
        BankAccount db_bankAccount = new BankAccount("test_bankAccountId", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.APPROVED, true, new Date(), new Date());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_walletAccount);
        Mockito.when(bankAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_bankAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getFromAccountId());
        Mockito.verify(bankAccountService).findById(createTransferInput.getToAccountId());
    }

    @DisplayName("should Return Null when User Is Exist And IsDeleted False And TransferType Is Withdraw And FromAccountId Is Exist In WalletAccount And ToAccountId Is Exist In BankAccount And BankAccount IsDeleted False And WalletAccount Status Not Equals Approved From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsWithdrawAndFromAccountIdIsExistInWalletAccountAndToAccountIdIsExistInBankAccountAndBankAccountIsDeletedFalseAndWalletAccountStatusNotEqualsApprovedFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_fromAccountId", "test_toAccountId", 25, TransferType.WITHDRAW, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 100, WalletAccountStatus.WAITING, false, new Date(), new Date());
        BankAccount db_bankAccount = new BankAccount("test_bankAccountId", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.APPROVED, false, new Date(), new Date());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_walletAccount);
        Mockito.when(bankAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_bankAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getFromAccountId());
        Mockito.verify(bankAccountService).findById(createTransferInput.getToAccountId());
    }

    @DisplayName("should Return Null when User Is Exist And IsDeleted False And TransferType Is Withdraw And FromAccountId Is Exist In WalletAccount And ToAccountId Is Exist In BankAccount And BankAccount IsDeleted False And WalletAccountStatus Equals Approved And WalletAccountBalance Less Then CreateTransferInput Amount From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsWithdrawAndFromAccountIdIsExistInWalletAccountAndToAccountIdIsExistInBankAccountAndBankAccountIsDeletedFalseAndWalletAccountStatusEqualsApprovedAndWalletAccountBalanceLessThenCreateTransferInputAmountFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_fromAccountId", "test_toAccountId", 25, TransferType.WITHDRAW, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 24, WalletAccountStatus.APPROVED, false, new Date(), new Date());
        BankAccount db_bankAccount = new BankAccount("test_bankAccountId", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.APPROVED, false, new Date(), new Date());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_walletAccount);
        Mockito.when(bankAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_bankAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getFromAccountId());
        Mockito.verify(bankAccountService).findById(createTransferInput.getToAccountId());
    }

    @DisplayName("should Return CreateTransferPayload And TransferType Equals Deposit when User Is Exist And IsDeleted False And TransferType Is Deposit And ToAccountId Is Exist In WalletAccount And FromAccountId Is Exist In BankAccount And WalletAccountStatus Equals Approved And BankAccount IsDeleted False And CreateTransferInput Amount Bigger Than Zero FromCreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnCreateTransferPayloadAndTransferTypeEqualsDeposit_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsDepositAndToAccountIdIsExistInWalletAccountAndFromAccountIdIsExistInBankAccountAndWalletAccountStatusEqualsApprovedAndBankAccountIsDeletedFalseAndCreateTransferInputAmountBiggerThanZeroFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_fromAccountId", "test_toAccountId", 25, TransferType.DEPOSIT, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 100, WalletAccountStatus.APPROVED, false, new Date(), new Date());
        BankAccount db_bankAccount = new BankAccount("test_bankAccountId", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.APPROVED, false, new Date(), new Date());
        Transfer transfer = new Transfer(null, "test_userId", "test_bankAccountId", "test_walletAccountId", 25, TransferStatus.WAITING, TransferType.DEPOSIT, false, createTransferInput.getDate());
        Transfer savedTransfer = new Transfer("test_transferId", "test_userId", "test_bankAccountId", "test_walletAccountId", 25, TransferStatus.WAITING, TransferType.DEPOSIT, false, createTransferInput.getDate());
        CreateTransferPayload createTransferPayload = new CreateTransferPayload("test_transferId", "test_bankAccountId", "test_walletAccountId", 25, TransferStatus.WAITING, TransferType.DEPOSIT, createTransferInput.getDate());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_walletAccount);
        Mockito.when(bankAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_bankAccount);
        Mockito.when(transferRepository.save(transfer)).thenReturn(savedTransfer);
        Mockito.when(mapperService.convertToCreateTransferPayload(savedTransfer)).thenReturn(createTransferPayload);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertEquals(expectedResult.getType(), createTransferPayload.getType());

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getToAccountId());
        Mockito.verify(bankAccountService).findById(createTransferInput.getFromAccountId());
        Mockito.verify(transferRepository).save(transfer);
        Mockito.verify(mapperService).convertToCreateTransferPayload(savedTransfer);

    }

    @DisplayName("should Return Null when User Is Exist And IsDeleted False And TransferType Is Deposit And ToAccountId Is Exist In WalletAccount And FromAccountId Is Exist In BankAccount And WalletAccount Status Equals Approved And BankAccount IsDeleted False And CreateTransferInput Amount Equals Or Less Than Zero From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsDepositAndToAccountIdIsExistInWalletAccountAndFromAccountIdIsExistInBankAccountAndWalletAccountStatusEqualsApprovedAndBankAccountIsDeletedFalseAndCreateTransferInputAmountEqualsOrLessThanZeroFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_fromAccountId", "test_toAccountId", 0, TransferType.DEPOSIT, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 100, WalletAccountStatus.APPROVED, false, new Date(), new Date());
        BankAccount db_bankAccount = new BankAccount("test_bankAccountId", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.APPROVED, false, new Date(), new Date());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_walletAccount);
        Mockito.when(bankAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_bankAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getToAccountId());
        Mockito.verify(bankAccountService).findById(createTransferInput.getFromAccountId());
    }

    @DisplayName("should Return Null when User Is Exist And IsDeleted False And TransferType Is Deposit And ToAccountId Is Exist In WalletAccount And FromAccountId Is Exist In BankAccount And WalletAccount Status Equals Approved And BankAccount IsDeleted True FromCreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsDepositAndToAccountIdIsExistInWalletAccountAndFromAccountIdIsExistInBankAccountAndWalletAccountStatusEqualsApprovedAndBankAccountIsDeletedTrueFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_fromAccountId", "test_toAccountId", 25, TransferType.DEPOSIT, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 100, WalletAccountStatus.APPROVED, false, new Date(), new Date());
        BankAccount db_bankAccount = new BankAccount("test_bankAccountId", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.APPROVED, true, new Date(), new Date());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_walletAccount);
        Mockito.when(bankAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_bankAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getToAccountId());
        Mockito.verify(bankAccountService).findById(createTransferInput.getFromAccountId());
    }

    @DisplayName("should Return Null when User Is Exist And IsDeleted False And TransferType Is Deposit And ToAccountId Is Exist In WalletAccount And FromAccountId Is Exist In BankAccount And WalletAccount Status Not Equals Approved From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsDepositAndToAccountIdIsExistInWalletAccountAndFromAccountIdIsExistInBankAccountAndWalletAccountStatusNotEqualsApprovedFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_fromAccountId", "test_toAccountId", 25, TransferType.DEPOSIT, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 100, WalletAccountStatus.WAITING, false, new Date(), new Date());
        BankAccount db_bankAccount = new BankAccount("test_bankAccountId", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.APPROVED, true, new Date(), new Date());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_walletAccount);
        Mockito.when(bankAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_bankAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getToAccountId());
        Mockito.verify(bankAccountService).findById(createTransferInput.getFromAccountId());
    }

    @DisplayName("should Return Null when User Is Exist And IsDeleted False And TransferType Is Deposit And ToAccountId Is Exist In WalletAccount And FromAccountId Does Not Exist In BankAccount From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsDepositAndToAccountIdIsExistInWalletAccountAndFromAccountIdDoesNotExistInBankAccountFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_fromAccountId", "test_toAccountId", 25, TransferType.DEPOSIT, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 100, WalletAccountStatus.WAITING, false, new Date(), new Date());
        //BankAccount db_bankAccount = new BankAccount("test_bankAccountId","test_userId","test_alias",BankName.TEST,"test_iban",Currency.TEST,"test_swiftCode",BankAccountStatus.APPROVED,true,new Date(),new Date());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_walletAccount);
        Mockito.when(bankAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(null);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getToAccountId());
        Mockito.verify(bankAccountService).findById(createTransferInput.getFromAccountId());
    }

    @DisplayName("should Return Null when User Is Exist And IsDeleted False And TransferType Is Deposit And ToAccountId Does Not Exist In WalletAccount And FromAccountId Is Exist In BankAccount From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsDepositAndToAccountIdDoesNotExistInWalletAccountAndFromAccountIdIsExistInBankAccountFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_fromAccountId", "test_toAccountId", 25, TransferType.DEPOSIT, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        //WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId","test_userId", Currency.TEST,100,WalletAccountStatus.WAITING,false,new Date(),new Date());
        BankAccount db_bankAccount = new BankAccount("test_bankAccountId", "test_userId", "test_alias", BankName.TEST, "test_iban", Currency.TEST, "test_swiftCode", BankAccountStatus.APPROVED, true, new Date(), new Date());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getToAccountId())).thenReturn(null);
        Mockito.when(bankAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_bankAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getToAccountId());
        Mockito.verify(bankAccountService).findById(createTransferInput.getFromAccountId());
    }

    @DisplayName("should Return CreateTransferPayload And TransferType Equals VirementToTradingAccount when User Is Exist And IsDeleted False And TransferType Is VirementToTradingAccount And FromAccountId Is Exist In WalletAccount And ToAccountId Is Exist In TradingAccount And TradingAccount Is Deleted False And WalletAccount Status Equals Approved And WalletAccount Balance Equals Or Bigger Then CreateTransferInput Amount From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnCreateTransferPayloadAndTransferTypeEqualsVirementToTradingAccount_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsVirementToTradingAccountAndFromAccountIdIsExistInWalletAccountAndToAccountIdIsExistInTradingAccountAndTradingAccountIsDeletedFalseAndWalletAccountStatusEqualsApprovedAndWalletAccountBalanceEqualsOrBiggerThenCreateTransferInputAmountFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_walletAccountId", "test_tradingAccountId", 25, TransferType.VIREMENT_TO_TRADING_ACCOUNT, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 100, WalletAccountStatus.APPROVED, false, new Date(), new Date());
        TradingAccount db_tradingAccount = new TradingAccount("test_tradingAccountId", "test_userId", "test_apiId", "test_walletAccountId", Currency.TEST, 0, 100, TradingAccountClassification.TEST, TradingAccountType.TEST, TradingAccountStatus.APPROVED, false, createTransferInput.getDate(), createTransferInput.getDate());
        Transfer transfer = new Transfer(null, "test_userId", "test_walletAccountId", "test_tradingAccountId", createTransferInput.getAmount(), TransferStatus.APPROVED, TransferType.VIREMENT_TO_TRADING_ACCOUNT, false, createTransferInput.getDate());
        Transfer savedTransfer = new Transfer("test_transferId", "test_userId", "test_walletAccountId", "test_tradingAccountId", transfer.getAmount(), TransferStatus.APPROVED, TransferType.VIREMENT_TO_TRADING_ACCOUNT, false, createTransferInput.getDate());
        CreateTransferPayload createTransferPayload = new CreateTransferPayload("test_transferId", "test_walletAccountId", "test_tradingAccountId", 10, TransferStatus.APPROVED, TransferType.VIREMENT_TO_TRADING_ACCOUNT, createTransferInput.getDate());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_walletAccount);
        Mockito.when(tradingAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_tradingAccount);
        Mockito.when(transferRepository.save(transfer)).thenReturn(savedTransfer);
        Mockito.when(mapperService.convertToCreateTransferPayload(savedTransfer)).thenReturn(createTransferPayload);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertSame(expectedResult, createTransferPayload);
        assertEquals(db_tradingAccount.getBalance(), transfer.getAmount());

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getFromAccountId());
        Mockito.verify(tradingAccountService).findById(createTransferInput.getToAccountId());
        Mockito.verify(transferRepository).save(transfer);
        Mockito.verify(tradingAccountService).save(db_tradingAccount);
        Mockito.verify(walletAccountService).save(db_walletAccount);
        Mockito.verify(mapperService).convertToCreateTransferPayload(savedTransfer);

    }

    @DisplayName("should Return Null when User Is Exist And IsDeleted False And TransferType Is VirementToTradingAccount And FromAccountId Is Exist In WalletAccount And ToAccountId Is Exist In TradingAccount And TradingAccount IsDeleted False And WalletAccount Status Equals Approved And WalletAccount Balance Less Then CreateTransferInput Amount FromCreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsVirementToTradingAccountAndFromAccountIdIsExistInWalletAccountAndToAccountIdIsExistInTradingAccountAndTradingAccountIsDeletedFalseAndWalletAccountStatusEqualsApprovedAndWalletAccountBalanceLessThenCreateTransferInputAmountFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_walletAccountId", "test_tradingAccountId", 25, TransferType.VIREMENT_TO_TRADING_ACCOUNT, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 24, WalletAccountStatus.APPROVED, false, new Date(), new Date());
        TradingAccount db_tradingAccount = new TradingAccount("test_tradingAccountId", "test_userId", "test_apiId", "test_walletAccountId", Currency.TEST, 0, 100, TradingAccountClassification.TEST, TradingAccountType.TEST, TradingAccountStatus.APPROVED, false, createTransferInput.getDate(), createTransferInput.getDate());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_walletAccount);
        Mockito.when(tradingAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_tradingAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getFromAccountId());
        Mockito.verify(tradingAccountService).findById(createTransferInput.getToAccountId());
    }

    @DisplayName("should Return Null when User Is Exist And IsDeleted False And TransferType Is VirementToTradingAccount And FromAccountId Is Exist In WalletAccount And ToAccountId Is Exist In TradingAccount And TradingAccount IsDeleted False And WalletAccount Status Equals Not Approved From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsVirementToTradingAccountAndFromAccountIdIsExistInWalletAccountAndToAccountIdIsExistInTradingAccountAndTradingAccountIsDeletedFalseAndWalletAccountStatusEqualsNotApprovedFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_walletAccountId", "test_tradingAccountId", 25, TransferType.VIREMENT_TO_TRADING_ACCOUNT, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 24, WalletAccountStatus.WAITING, false, new Date(), new Date());
        TradingAccount db_tradingAccount = new TradingAccount("test_tradingAccountId", "test_userId", "test_apiId", "test_walletAccountId", Currency.TEST, 0, 100, TradingAccountClassification.TEST, TradingAccountType.TEST, TradingAccountStatus.APPROVED, false, createTransferInput.getDate(), createTransferInput.getDate());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_walletAccount);
        Mockito.when(tradingAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_tradingAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getFromAccountId());
        Mockito.verify(tradingAccountService).findById(createTransferInput.getToAccountId());

    }

    @DisplayName("should Return Null when User Is Exist And IsDeleted False And TransferType Is VirementToTradingAccount And FromAccountId Is Exist In WalletAccount And ToAccountId Is Exist In TradingAccount And TradingAccount IsDeleted True From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsVirementToTradingAccountAndFromAccountIdIsExistInWalletAccountAndToAccountIdIsExistInTradingAccountAndTradingAccountIsDeletedTrueFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_walletAccountId", "test_tradingAccountId", 25, TransferType.VIREMENT_TO_TRADING_ACCOUNT, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 24, WalletAccountStatus.WAITING, false, new Date(), new Date());
        TradingAccount db_tradingAccount = new TradingAccount("test_tradingAccountId", "test_userId", "test_apiId", "test_walletAccountId", Currency.TEST, 0, 100, TradingAccountClassification.TEST, TradingAccountType.TEST, TradingAccountStatus.APPROVED, true, createTransferInput.getDate(), createTransferInput.getDate());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_walletAccount);
        Mockito.when(tradingAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_tradingAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getFromAccountId());
        Mockito.verify(tradingAccountService).findById(createTransferInput.getToAccountId());
    }

    @DisplayName("should Return Null when User Is Exist And IsDeleted False And TransferType Is VirementToTradingAccount And FromAccountId Is Exist In WalletAccountAnd ToAccountId Does Not Exist In TradingAccount From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsVirementToTradingAccountAndFromAccountIdIsExistInWalletAccountAndToAccountIdDoesNotExistInTradingAccountFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_walletAccountId", "test_tradingAccountId", 25, TransferType.VIREMENT_TO_TRADING_ACCOUNT, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 24, WalletAccountStatus.WAITING, false, new Date(), new Date());
        //TradingAccount db_tradingAccount = new TradingAccount("test_tradingAccountId","test_userId","test_apiId","test_walletAccountId",Currency.TEST,0,100,TradingAccountClassification.TEST,TradingAccountType.TEST,TradingAccountStatus.APPROVED,true,createTransferInput.getDate(),createTransferInput.getDate());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_walletAccount);
        Mockito.when(tradingAccountService.findById(createTransferInput.getToAccountId())).thenReturn(null);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getFromAccountId());
        Mockito.verify(tradingAccountService).findById(createTransferInput.getToAccountId());
    }

    @DisplayName("should Return Null when User Is Exist And IsDeleted False And TransferType Is VirementToTradingAccount And FromAccountId Does Not Exist In WalletAccount And ToAccount Id Is Exist In TradingAccount From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsVirementToTradingAccountAndFromAccountIdDoesNotExistInWalletAccountAndToAccountIdIsExistInTradingAccountFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_walletAccountId", "test_tradingAccountId", 25, TransferType.VIREMENT_TO_TRADING_ACCOUNT, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        TradingAccount db_tradingAccount = new TradingAccount("test_tradingAccountId", "test_userId", "test_apiId", "test_walletAccountId", Currency.TEST, 0, 100, TradingAccountClassification.TEST, TradingAccountType.TEST, TradingAccountStatus.APPROVED, true, createTransferInput.getDate(), createTransferInput.getDate());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(null);
        Mockito.when(tradingAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_tradingAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getFromAccountId());
        Mockito.verify(tradingAccountService).findById(createTransferInput.getToAccountId());
    }

    @DisplayName("should Return CreateTransferPayload And TransferType Equals VirementToWallet when User Is Exist And IsDeleted False And TransferType Is VirementToWallet And ToAccountId Is Exist In WalletAccount And FromAccountId Is Exist In TradingAccount And TradingAccount IsDeleted False And WalletAccount Status Equals Approved And TradingAccount Balance Equals Or Bigger Than CreateTransferInputAmount From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnCreateTransferPayloadAndTransferTypeEqualsVirementToWallet_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsVirementToWalletAndToAccountIdIsExistInWalletAccountAndFromAccountIdIsExistInTradingAccountAndTradingAccountIsDeletedFalseAndWalletAccountStatusEqualsApprovedAndTradingAccountBalanceEqualsOrBiggerThanCreateTransferInputAmountFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_tradingAccountId", "test_walletAccountId", 25, TransferType.VIREMENT_TO_WALLET, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 100, WalletAccountStatus.APPROVED, false, new Date(), new Date());
        TradingAccount db_tradingAccount = new TradingAccount("test_tradingAccountId", "test_userId", "test_apiId", "test_walletAccountId", Currency.TEST, 90, 100, TradingAccountClassification.TEST, TradingAccountType.TEST, TradingAccountStatus.APPROVED, false, createTransferInput.getDate(), createTransferInput.getDate());
        Transfer transfer = new Transfer(null, "test_userId", "test_tradingAccountId", "test_walletAccountId", createTransferInput.getAmount(), TransferStatus.APPROVED, TransferType.VIREMENT_TO_WALLET, false, createTransferInput.getDate());
        Transfer savedTransfer = new Transfer("test_transferId", "test_userId", "test_tradingAccountId", "test_walletAccountId", createTransferInput.getAmount(), TransferStatus.APPROVED, TransferType.VIREMENT_TO_WALLET, false, createTransferInput.getDate());
        CreateTransferPayload createTransferPayload = new CreateTransferPayload("test_transferId", "test_tradingAccountId", "test_walletAccountId", createTransferInput.getAmount(), TransferStatus.APPROVED, TransferType.VIREMENT_TO_WALLET, createTransferInput.getDate());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_walletAccount);
        Mockito.when(tradingAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_tradingAccount);
        Mockito.when(transferRepository.save(transfer)).thenReturn(savedTransfer);
        Mockito.when(mapperService.convertToCreateTransferPayload(savedTransfer)).thenReturn(createTransferPayload);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertSame(expectedResult, createTransferPayload);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService, Mockito.times(2)).findById(createTransferInput.getToAccountId());
        Mockito.verify(tradingAccountService, Mockito.times(2)).findById(createTransferInput.getFromAccountId());
        Mockito.verify(transferRepository).save(transfer);
        Mockito.verify(tradingAccountService).save(db_tradingAccount);
        Mockito.verify(walletAccountService).save(db_walletAccount);
        Mockito.verify(mapperService).convertToCreateTransferPayload(savedTransfer);

    }

    @DisplayName("should Return CreateTransferPayload And TransferType Equals VirementToWallet when User Is Exist And IsDeleted False And TransferType Is VirementToWallet And ToAccountId Is Exist In WalletAccount And FromAccountId Is Exist In TradingAccount And TradingAccount IsDeleted False And WalletAccount Status Equals Approved And TradingAccount Balance Less Than CreateTransferInputAmount From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsVirementToWalletAndToAccountIdIsExistInWalletAccountAndFromAccountIdIsExistInTradingAccountAndTradingAccountIsDeletedFalseAndWalletAccountStatusEqualsApprovedAndTradingAccountBalanceLessThanCreateTransferInputAmountFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_tradingAccountId", "test_walletAccountId", 95, TransferType.VIREMENT_TO_WALLET, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 100, WalletAccountStatus.APPROVED, false, new Date(), new Date());
        TradingAccount db_tradingAccount = new TradingAccount("test_tradingAccountId", "test_userId", "test_apiId", "test_walletAccountId", Currency.TEST, 90, 100, TradingAccountClassification.TEST, TradingAccountType.TEST, TradingAccountStatus.APPROVED, false, createTransferInput.getDate(), createTransferInput.getDate());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_walletAccount);
        Mockito.when(tradingAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_tradingAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getToAccountId());
        Mockito.verify(tradingAccountService).findById(createTransferInput.getFromAccountId());

    }

    @DisplayName("should Return CreateTransferPayload And TransferType Equals VirementToWallet when User Is Exist And IsDeleted False And TransferType Is VirementToWallet And ToAccountId Is Exist In WalletAccount And FromAccountId Is Exist In TradingAccount And TradingAccount IsDeleted False And WalletAccount Status Not Equals Approved From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsVirementToWalletAndToAccountIdIsExistInWalletAccountAndFromAccountIdIsExistInTradingAccountAndTradingAccountIsDeletedFalseAndWalletAccountStatusNotEqualsApprovedFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_tradingAccountId", "test_walletAccountId", 95, TransferType.VIREMENT_TO_WALLET, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 100, WalletAccountStatus.WAITING, false, new Date(), new Date());
        TradingAccount db_tradingAccount = new TradingAccount("test_tradingAccountId", "test_userId", "test_apiId", "test_walletAccountId", Currency.TEST, 90, 100, TradingAccountClassification.TEST, TradingAccountType.TEST, TradingAccountStatus.APPROVED, false, createTransferInput.getDate(), createTransferInput.getDate());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_walletAccount);
        Mockito.when(tradingAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_tradingAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getToAccountId());
        Mockito.verify(tradingAccountService).findById(createTransferInput.getFromAccountId());

    }

    @DisplayName("should Return CreateTransferPayload And TransferType Equals VirementToWallet when User Is Exist And IsDeleted False And TransferType Is VirementToWallet And ToAccountId Is Exist In WalletAccount And FromAccountId Is Exist In TradingAccount And TradingAccount IsDeleted True From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsVirementToWalletAndToAccountIdIsExistInWalletAccountAndFromAccountIdIsExistInTradingAccountAndTradingAccountIsDeletedTrueFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_tradingAccountId", "test_walletAccountId", 95, TransferType.VIREMENT_TO_WALLET, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 100, WalletAccountStatus.WAITING, false, new Date(), new Date());
        TradingAccount db_tradingAccount = new TradingAccount("test_tradingAccountId", "test_userId", "test_apiId", "test_walletAccountId", Currency.TEST, 90, 100, TradingAccountClassification.TEST, TradingAccountType.TEST, TradingAccountStatus.APPROVED, true, createTransferInput.getDate(), createTransferInput.getDate());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_walletAccount);
        Mockito.when(tradingAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_tradingAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getToAccountId());
        Mockito.verify(tradingAccountService).findById(createTransferInput.getFromAccountId());
    }

    @DisplayName("should Return CreateTransferPayload And TransferType Equals VirementToWallet when User Is Exist And IsDeleted False And TransferType Is VirementToWallet And ToAccountId Is Exist In WalletAccount And FromAccountId Does Not Exist In TradingAccount From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsVirementToWalletAndToAccountIdIsExistInWalletAccountAndFromAccountIdDoesNotExistInTradingAccountFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_tradingAccountId", "test_walletAccountId", 95, TransferType.VIREMENT_TO_WALLET, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        WalletAccount db_walletAccount = new WalletAccount("test_walletAccountId", "test_userId", Currency.TEST, 100, WalletAccountStatus.WAITING, false, new Date(), new Date());


        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getToAccountId())).thenReturn(db_walletAccount);
        Mockito.when(tradingAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(null);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getToAccountId());
        Mockito.verify(tradingAccountService).findById(createTransferInput.getFromAccountId());
    }

    @DisplayName("should Return CreateTransferPayload And TransferType Equals VirementToWallet when User Is Exist And IsDeleted False And TransferType Is VirementToWallet And ToAccountId Does Not Exist In WalletAccount And FromAccountId Is Exist In TradingAccount From CreateTransferInput")
    @Tag("createTransfer")
    @Test
    void shouldReturnNull_whenUserIsExistAndIsDeletedFalseAndTransferTypeIsVirementToWalletAndToAccountIdDoesNotExistInWalletAccountAndFromAccountIdIsExistInTradingAccountFromCreateTransferInput() throws InterruptedException {
        CreateTransferInput createTransferInput = new CreateTransferInput("test_userId", "test_tradingAccountId", "test_walletAccountId", 95, TransferType.VIREMENT_TO_WALLET, new Date());
        User user = new User("test_userId", "test_email", "test_password", "test_name", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        TradingAccount db_tradingAccount = new TradingAccount("test_tradingAccountId", "test_userId", "test_apiId", "test_walletAccountId", Currency.TEST, 90, 100, TradingAccountClassification.TEST, TradingAccountType.TEST, TradingAccountStatus.APPROVED, true, createTransferInput.getDate(), createTransferInput.getDate());

        Mockito.when(customUserService.findByUserId(createTransferInput.getUserId())).thenReturn(user);
        Mockito.when(walletAccountService.findById(createTransferInput.getToAccountId())).thenReturn(null);
        Mockito.when(tradingAccountService.findById(createTransferInput.getFromAccountId())).thenReturn(db_tradingAccount);

        CreateTransferPayload expectedResult = transferService.createTransfer(createTransferInput);

        assertNull(expectedResult);

        Mockito.verify(customUserService).findByUserId(createTransferInput.getUserId());
        Mockito.verify(walletAccountService).findById(createTransferInput.getToAccountId());
        Mockito.verify(tradingAccountService).findById(createTransferInput.getFromAccountId());
    }

    @DisplayName("should Return DeleteTransferPayload Status True when TransferId Is Exist In Transfer And Transfer IsDeleted False And TransferType Equals Deposit And Transfer Status Not Equals Approved From DeleteTransferInput")
    @Tag("deleteTransfer")
    @Test
    void shouldReturnDeleteTransferPayloadStatusTrue_whenTransferIdIsExistInTransferAndTransferIsDeletedFalseAndTransferTypeEqualsDepositAndTransferStatusNotEqualsApprovedFromDeleteTransferInput() {
        DeleteTransferInput deleteTransferInput = new DeleteTransferInput("test_transferId");
        Transfer transfer = new Transfer("test_transferId", "test_userId", "test_tradingAccountId", "test_walletAccountId", 100, TransferStatus.DENIED, TransferType.DEPOSIT, false, new Date());
        Transfer savedTransfer = new Transfer("test_transferId", "test_userId", "test_tradingAccountId", "test_walletAccountId", 100, TransferStatus.DENIED, TransferType.DEPOSIT, true, transfer.getCreateDate());

        Mockito.when(transferRepository.findById(deleteTransferInput.getId())).thenReturn(Optional.of(transfer));
        Mockito.when(transferRepository.save(transfer)).thenReturn(savedTransfer);

        DeleteTransferPayload expectedResult = transferService.deleteTransfer(deleteTransferInput);

        assertTrue(expectedResult.isStatus());

        Mockito.verify(transferRepository).findById(deleteTransferInput.getId());
        Mockito.verify(transferRepository).save(transfer);
    }

    @DisplayName("should Return DeleteTransferPayload Status True when TransferId Is Exist In Transfer And Transfer IsDeleted False And TransferType Equals Deposit And Transfer Status Equals Approved From DeleteTransferInput")
    @Tag("deleteTransfer")
    @Test
    void shouldReturnDeleteTransferPayloadStatusFalse_whenTransferIdIsExistInTransferAndTransferIsDeletedFalseAndTransferTypeEqualsDepositAndTransferStatusEqualsApprovedFromDeleteTransferInput() {
        DeleteTransferInput deleteTransferInput = new DeleteTransferInput("test_transferId");
        Transfer transfer = new Transfer("test_transferId", "test_userId", "test_tradingAccountId", "test_walletAccountId", 100, TransferStatus.APPROVED, TransferType.DEPOSIT, false, new Date());

        Mockito.when(transferRepository.findById(deleteTransferInput.getId())).thenReturn(Optional.of(transfer));

        DeleteTransferPayload expectedResult = transferService.deleteTransfer(deleteTransferInput);

        assertFalse(expectedResult.isStatus());

        Mockito.verify(transferRepository).findById(deleteTransferInput.getId());

    }

    @DisplayName("should Return DeleteTransferPayload Status True when TransferId Is Exist In Transfer And Transfer IsDeleted False And TransferType Is Not Equals Deposit From DeleteTransferInput")
    @Tag("deleteTransfer")
    @Test
    void shouldReturnDeleteTransferPayloadStatusFalse_whenTransferIdIsExistInTransferAndTransferIsDeletedFalseAndTransferTypeIsNotEqualsDepositFromDeleteTransferInput() {
        DeleteTransferInput deleteTransferInput = new DeleteTransferInput("test_transferId");
        Transfer transfer = new Transfer("test_transferId", "test_userId", "test_tradingAccountId", "test_walletAccountId", 100, TransferStatus.APPROVED, TransferType.TEST, false, new Date());

        Mockito.when(transferRepository.findById(deleteTransferInput.getId())).thenReturn(Optional.of(transfer));

        DeleteTransferPayload expectedResult = transferService.deleteTransfer(deleteTransferInput);

        assertFalse(expectedResult.isStatus());

        Mockito.verify(transferRepository).findById(deleteTransferInput.getId());
    }

    @DisplayName("should Return DeleteTransferPayload Status True when TransferId Is Exist In Transfer And Transfer IsDeleted True From DeleteTransferInput")
    @Tag("deleteTransfer")
    @Test
    void shouldReturnDeleteTransferPayloadStatusFalse_whenTransferIdIsExistInTransferAndTransferIsDeletedTrueFromDeleteTransferInput() {
        DeleteTransferInput deleteTransferInput = new DeleteTransferInput("test_transferId");
        Transfer transfer = new Transfer("test_transferId", "test_userId", "test_tradingAccountId", "test_walletAccountId", 100, TransferStatus.APPROVED, TransferType.DEPOSIT, true, new Date());

        Mockito.when(transferRepository.findById(deleteTransferInput.getId())).thenReturn(Optional.of(transfer));

        DeleteTransferPayload expectedResult = transferService.deleteTransfer(deleteTransferInput);

        assertFalse(expectedResult.isStatus());

        Mockito.verify(transferRepository).findById(deleteTransferInput.getId());
    }

    @DisplayName("should Return DeleteTransferPayload Status True when TransferId Does Not Exist In Transfer From DeleteTransferInput")
    @Tag("deleteTransfer")
    @Test
    void shouldReturnDeleteTransferPayloadStatusFalse_whenTransferIdDoesNotExistInTransferFromDeleteTransferInput() {
        DeleteTransferInput deleteTransferInput = new DeleteTransferInput("test_transferId");

        Mockito.when(transferRepository.findById(deleteTransferInput.getId())).thenReturn(Optional.empty());

        DeleteTransferPayload expectedResult = transferService.deleteTransfer(deleteTransferInput);

        assertFalse(expectedResult.isStatus());

        Mockito.verify(transferRepository).findById(deleteTransferInput.getId());
    }

    @AfterEach
    void tearDown() {
    }
}
