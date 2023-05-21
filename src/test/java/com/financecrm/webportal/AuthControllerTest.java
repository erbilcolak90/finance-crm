package com.financecrm.webportal;

import com.financecrm.webportal.auth.CustomUserDetailsService;
import com.financecrm.webportal.auth.TokenManager;
import com.financecrm.webportal.controller.AuthController;
import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.enums.UserStatus;
import com.financecrm.webportal.input.login.LoginInput;
import com.financecrm.webportal.payload.auth.LoginPayload;
import com.financecrm.webportal.services.CustomUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenManager tokenManager;
    @Mock
    private CustomUserService customUserService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private CustomUserDetailsService customUserDetailsService;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithUserDetails("test_email")
    void shouldReturnResponseEntityPayloadLoginPayload_whenEmailAndPasswordIsAuthenticatedFromLoginInput(){
        LoginInput loginInput = new LoginInput("test_email","test_password");
        String token = "test_token";
        String userId = " test_userId";
        User user = new User("test_userId","test_email","test_password","test_name","test_surname","test_phone","test_representativeEmployeeId", UserStatus.APPROVED,false,new Date(),new Date());
        LoginPayload loginPayload = new LoginPayload("test_token","test_userId");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginInput.getEmail(),loginInput.getPassword());
        AuthenticationProvider authenticationProvider = new TestingAuthenticationProvider();

        when(tokenManager.generateToken(loginInput.getEmail())).thenReturn(token);
        when(tokenManager.parseUserIdFromToken(token)).thenReturn(userId);

        ResponseEntity<LoginPayload> expectedResult = authController.login(loginInput);
        LoginPayload expectedLoginPayload = expectedResult.getBody();

        assertNotNull(expectedResult);
        assertEquals(200,expectedResult.getStatusCodeValue());
        assertNotNull(expectedLoginPayload);
        assertEquals(expectedLoginPayload.getToken(),token);
        assertEquals(expectedLoginPayload.getUserId(),userId);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        //verify(authentication).isAuthenticated();
        verify(tokenManager).generateToken(loginInput.getEmail());
        verify(tokenManager).parseUserIdFromToken(token);

    }

    @AfterEach
    void tearDown() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }
}
