package com.testtask.phonecontacts.controller;

import com.testtask.phonecontacts.controller.api.AuthenticationAPI;
import com.testtask.phonecontacts.model.LoginRequest;
import com.testtask.phonecontacts.model.LoginResponse;
import com.testtask.phonecontacts.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthenticationAPI {

    private final AuthService authService;

    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.loginUser(request.getUsername(), request.getPassword());
    }

    public LoginResponse register(@RequestBody LoginRequest request) {
        return authService.registerUser(request.getUsername(), request.getPassword());
    }
}
