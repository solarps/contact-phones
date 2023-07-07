package com.testtask.phonecontacts.service;

import com.testtask.phonecontacts.model.LoginResponse;
import com.testtask.phonecontacts.persistance.UserRepository;
import com.testtask.phonecontacts.persistance.entity.Role;
import com.testtask.phonecontacts.persistance.entity.User;
import com.testtask.phonecontacts.security.JwtService;
import com.testtask.phonecontacts.security.UserPrincipal;
import com.testtask.phonecontacts.security.UserPrincipalAuthenticationToken;
import com.testtask.phonecontacts.service.exceptions.EntityExistsException;
import com.testtask.phonecontacts.util.TestLoginUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthServiceTest {

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void setupAuthenticationManager() {
        UserPrincipal principal = UserPrincipal.builder()
                .username("testuser")
                .password(passwordEncoder.encode("testpassword"))
                .authorities(List.of(new SimpleGrantedAuthority(Role.USER.name())))
                .build();

        Authentication authentication = new UserPrincipalAuthenticationToken(principal);

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
    }

    @Test
    void testLoginUser() {
        String username = "testuser";
        String password = "testpassword";

        User user = new User(username, passwordEncoder.encode(password), Role.USER);
        UserDetails userDetails = UserPrincipal.fromUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        LoginResponse expectedResponse = TestLoginUtil.createResponse();

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(jwtService.assemble(any(), eq(username), anyList())).thenReturn(expectedResponse.getToken());


        LoginResponse response = authService.loginUser(username, password);

        assertEquals(expectedResponse.getToken(), response.getToken());
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(jwtService).assemble(any(), eq(username), anyList());
    }

    @Test
    void testRegisterUser() {
        String username = "testuser";
        String password = "testpassword";

        when(userRepository.existsByUsername(username)).thenReturn(false);

        User savedUser = new User(username, passwordEncoder.encode(password), Role.USER);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        LoginResponse expectedResponse = TestLoginUtil.createResponse();
        when(jwtService.assemble(any(), eq(username), anyList())).thenReturn(expectedResponse.getToken());

        LoginResponse response = authService.registerUser(username, password);

        assertEquals(expectedResponse.getToken(), response.getToken());
        verify(userRepository).existsByUsername(username);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUser_UserExists() {
        String username = "testuser";
        String password = "testpassword";

        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> authService.registerUser(username, password));

        verify(userRepository).existsByUsername(username);
        verify(userRepository, never()).save(any(User.class));
    }
}