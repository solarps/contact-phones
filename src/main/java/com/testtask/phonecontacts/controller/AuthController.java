package com.testtask.phonecontacts.controller;

import com.testtask.phonecontacts.model.LoginRequest;
import com.testtask.phonecontacts.model.LoginResponse;
import com.testtask.phonecontacts.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.loginUser(request.getUsername(), request.getPassword());
    }

    @PostMapping("/auth/register")
    public LoginResponse register(@RequestBody LoginRequest request) {
        return authService.registerUser(request.getUsername(), request.getPassword());
    }
}
