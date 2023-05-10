package com.financecrm.webportal;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.enums.Role;
import com.financecrm.webportal.enums.UserStatus;
import com.financecrm.webportal.input.user.GetUserByIdInput;
import com.financecrm.webportal.input.user.SignUpInput;
import com.financecrm.webportal.input.userrole.AddRoleToUserInput;
import com.financecrm.webportal.payload.user.UserPayload;
import com.financecrm.webportal.repositories.UserRepository;
import com.financecrm.webportal.security.PasswordUtil;
import com.financecrm.webportal.services.CustomUserService;
import com.financecrm.webportal.services.MapperService;
import com.financecrm.webportal.services.UserRoleService;
import com.financecrm.webportal.services.WalletAccountService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomUserServiceTest {

    @InjectMocks
    private CustomUserService customUserService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleService userRoleService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private WalletAccountService walletAccountService;
    @Mock
    private MapperService mapperService;
    @Mock
    private PasswordUtil passwordUtil;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("should return user when username is exist")
    @Tag("findByName")
    @Test
    //1. adım : test isimlendirilmesi
    void shouldReturnUser_whenUsernameIsExist(){
        // 2. adım : test verilerinin hazırlanması
        String username = "test_username";
        User db_user = new User("test_userId", "test_email", "test_password", username, "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        // 3. adım : bağımlı değişkenlerin davranışlarının belirlenmesi.
        Mockito.when(userRepository.findByName(username)).thenReturn(Optional.of(db_user));
        // 4. adım : test edilecek metodun çalıştırılması
        User expectedResult = customUserService.findByName(username);
        // 5. adım : test sonuçlarının karşılaştırılması
        assertNotNull(expectedResult);
        assertEquals(expectedResult,db_user);
        // 6. adım : bağımlı değişkenlerin çalıştığının kontrolü
        Mockito.verify(userRepository).findByName(username);
    }
    @DisplayName("should return null when username does not exist")
    @Tag("findByName")
    @Test
        //1. adım : test isimlendirilmesi
    void shouldReturnNull_whenUsernameDoesNotExist(){
        // 2. adım : test verilerinin hazırlanması
        String username = "test_username";
        // 3. adım : bağımlı değişkenlerin davranışlarının belirlenmesi.
        Mockito.when(userRepository.findByName(username)).thenReturn(Optional.empty());
        // 4. adım : test edilecek metodun çalıştırılması
        User expectedResult = customUserService.findByName(username);
        // 5. adım : test sonuçlarının karşılaştırılması
        assertNull(expectedResult);
        // 6. adım : bağımlı değişkenlerin çalıştığının kontrolü
        Mockito.verify(userRepository).findByName(username);
    }
    //1. adım : test isimlendirilmesi
    @DisplayName("should return user when username is exist")
    @Tag("findByEmail")
    @Test
    void shouldReturnUser_whenEmailIsExist(){
        // 2. adım : test verilerinin hazırlanması
        String email = "test_email";
        User db_user = new User("test_userId", email, "test_password", "test_username", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        // 3. adım : bağımlı değişkenlerin davranışlarının belirlenmesi.
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(db_user));
        // 4. adım : test edilecek metodun çalıştırılması
        User expectedResult = customUserService.findByEmail(email);
        // 5. adım : test sonuçlarının karşılaştırılması
        assertNotNull(expectedResult);
        assertEquals(expectedResult,db_user);
        // 6. adım : bağımlı değişkenlerin çalıştığının kontrolü
        Mockito.verify(userRepository).findByEmail(email);
    }

    @DisplayName("should return null when username does not exist")
    @Tag("findByEmail")
    @Test
    void shouldReturnNull_whenEmailDoesNotExist(){
        // 2. adım : test verilerinin hazırlanması
        String email = "test_email";
        // 3. adım : bağımlı değişkenlerin davranışlarının belirlenmesi.
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        // 4. adım : test edilecek metodun çalıştırılması
        User expectedResult = customUserService.findByEmail(email);
        // 5. adım : test sonuçlarının karşılaştırılması
        assertNull(expectedResult);
        // 6. adım : bağımlı değişkenlerin çalıştığının kontrolü
        Mockito.verify(userRepository).findByEmail(email);
    }

    @DisplayName("should return user when userId is exist")
    @Tag("findByUserId")
    @Test
    void shouldReturnUser_whenUserIdIsExist(){
        // 2. adım : test verilerinin hazırlanması
        String userId = "test_userId";
        User db_user = new User(userId, "test_email", "test_password", "test_username", "test_surname", "test_phone", "test_representativeEmployeeId", UserStatus.TEST, false, new Date(), new Date());
        // 3. adım : bağımlı değişkenlerin davranışlarının belirlenmesi.
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(db_user));
        // 4. adım : test edilecek metodun çalıştırılması
        User expectedResult = customUserService.findByUserId(userId);
        // 5. adım : test sonuçlarının karşılaştırılması
        assertNotNull(expectedResult);
        assertEquals(expectedResult,db_user);
        // 6. adım : bağımlı değişkenlerin çalıştığının kontrolü
        Mockito.verify(userRepository).findById(userId);
    }

    @DisplayName("should return null when userId does not exist")
    @Tag("findByUserId")
    @Test
    void shouldReturnNull_whenUserIdDoesNotExist(){
        // 2. adım : test verilerinin hazırlanması
        String userId = "test_userId";
        // 3. adım : bağımlı değişkenlerin davranışlarının belirlenmesi.
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
        // 4. adım : test edilecek metodun çalıştırılması
        User expectedResult = customUserService.findByUserId(userId);
        // 5. adım : test sonuçlarının karşılaştırılması
        assertNull(expectedResult);
        // 6. adım : bağımlı değişkenlerin çalıştığının kontrolü
        Mockito.verify(userRepository).findById(userId);
    }

    // 1. adım : test isminin belirlenmesi
    @DisplayName("should return true when email from signupinput does not exist")
    @Tag("signUp")
    @Test
    void shouldReturnTrue_whenEmailFromSignUpInputDoesNotExist(){
        // 2. adım : test verilerinin hazırlanması
        SignUpInput signUpInput = new SignUpInput("test_name","test_surname","test_email","test_password","test_phone",new Date());
        String testPassword = PasswordUtil.hashPassword(signUpInput.getPassword());
        User user = new User(null,"test_email",testPassword,"test_name","test_surname","test_phone",null,UserStatus.WAITING,false,signUpInput.getDate(),signUpInput.getDate());
        User savedUser = new User("test_id","test_email",testPassword,"test_name","test_surname","test_phone",null,UserStatus.WAITING,false,signUpInput.getDate(),signUpInput.getDate());
        AddRoleToUserInput addRoleToUserInput = new AddRoleToUserInput(null, Role.USER.toString());

        // 3. adım : bağımlı değişkenlerin davranışlarının belirlenmesi.
        Mockito.when(userRepository.findByEmail(signUpInput.getEmail())).thenReturn(Optional.empty());
        Mockito.when(bCryptPasswordEncoder.encode(signUpInput.getPassword())).thenReturn(testPassword);
        Mockito.when(userRepository.save(user)).thenReturn(savedUser);
        // 4. adım : test metodunun çalıştırılması
        boolean expectedSignUpResult = customUserService.signUp(signUpInput);
        // 5. adım : test sonuçlarının karşılaştırılması
        assertTrue(expectedSignUpResult);
        assertEquals(user.getPassword(),testPassword);
        // 6. adım : bağımlı değişkenlerin çalıştırıldığının kontrolü.
        Mockito.verify(userRepository).findByEmail(signUpInput.getEmail());
        Mockito.verify(bCryptPasswordEncoder).encode(signUpInput.getPassword());
        Mockito.verify(userRepository).save(user);
        Mockito.verify(userRoleService).addRoleToUser(addRoleToUserInput);
        Mockito.verify(walletAccountService).createWalletAccount(user.getId());
    }

    @DisplayName("should return false when email from signupinput is exist ")
    @Tag("signUp")
    @Test
    void shouldReturnFalse_whenEmailFromSignUpInputIsExist(){
        SignUpInput signUpInput = new SignUpInput("test_name","test_surname","test_email","test_password","test_phone",new Date());
        User db_user = new User("test_id","test_email","test_password","test_name","test_surname","test_phone",null,UserStatus.WAITING,false,signUpInput.getDate(),signUpInput.getDate());

        Mockito.when(userRepository.findByEmail(signUpInput.getEmail())).thenReturn(Optional.of(db_user));

        boolean expectedSignUpResult = customUserService.signUp(signUpInput);

        assertFalse(expectedSignUpResult);

        Mockito.verify(userRepository).findByEmail(signUpInput.getEmail());
    }

    @DisplayName("should return userpayload when user_id getuserbyidinput is exist")
    @Tag("getUserById")
    @Test
    void shouldReturnUserPayload_whenUserIdFromGetUserByIdInputIsExist(){
        GetUserByIdInput getUserByIdInput = new GetUserByIdInput("test_userId");
        UserPayload userPayload = new UserPayload("test_id","test_name","test_surname","test_email","test_phone",UserStatus.TEST,new ArrayList<>());

        Mockito.when(mapperService.convertToUserPayload(getUserByIdInput)).thenReturn(userPayload);

        UserPayload expectedResult = customUserService.getUserById(getUserByIdInput);

        assertEquals(expectedResult,userPayload);

        Mockito.verify(mapperService).convertToUserPayload(getUserByIdInput);
    }

    @DisplayName("should return null when user_id from getuserbyidinput does not exist")
    @Tag("getUserById")
    @Test
    void shouldReturnNull_whenUserIdFromGetUserByIdInputDoesNotExist(){
        GetUserByIdInput getUserByIdInput = new GetUserByIdInput("test_userId");
        UserPayload userPayload = null;

        Mockito.when(mapperService.convertToUserPayload(getUserByIdInput)).thenReturn(null);

        UserPayload expectedResult = customUserService.getUserById(getUserByIdInput);

        assertNull(expectedResult);

        Mockito.verify(mapperService).convertToUserPayload(getUserByIdInput);
    }

    @AfterEach
    void tearDown() {
    }
}
