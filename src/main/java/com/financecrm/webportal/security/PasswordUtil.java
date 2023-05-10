package com.financecrm.webportal.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final String TEST_PASSWORD = "test1234";

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return bCryptPasswordEncoder.matches(password, hashedPassword);
    }

    public static String getTestPassword() {
        return TEST_PASSWORD;
    }

}
