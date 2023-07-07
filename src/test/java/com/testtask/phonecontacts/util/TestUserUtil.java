package com.testtask.phonecontacts.util;

import com.testtask.phonecontacts.persistance.entity.Role;
import com.testtask.phonecontacts.persistance.entity.User;

public class TestUserUtil {

    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "testpassword";

    public static User createUser() {
        return new User(USERNAME, PASSWORD, Role.USER);
    }
}
