package com.testtask.phonecontacts.util;

import com.testtask.phonecontacts.model.LoginRequest;
import com.testtask.phonecontacts.model.LoginResponse;

public class TestLoginUtil {
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "testpassword";
    private static final String TOKEN = "testtoken";

    public static LoginRequest createRequest() {
        return LoginRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();
    }

    public static LoginResponse createResponse() {
        return LoginResponse.builder().
                token(TOKEN)
                .build();
    }
}
