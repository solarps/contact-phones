package com.testtask.phonecontacts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testtask.phonecontacts.config.SecurityConfig;
import com.testtask.phonecontacts.model.LoginRequest;
import com.testtask.phonecontacts.model.LoginResponse;
import com.testtask.phonecontacts.service.AuthService;
import com.testtask.phonecontacts.util.TestLoginUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(SecurityConfig.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLogin() throws Exception {
        LoginRequest request = TestLoginUtil.createRequest();

        LoginResponse expectedResponse = TestLoginUtil.createResponse();

        when(authService.loginUser(request.getUsername(), request.getPassword())).thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value(expectedResponse.getToken()));
    }

    @Test
    void testRegister() throws Exception {
        LoginRequest request = TestLoginUtil.createRequest();

        LoginResponse expectedResponse = TestLoginUtil.createResponse();

        when(authService.registerUser(request.getUsername(), request.getPassword())).thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value(expectedResponse.getToken()));
    }
}