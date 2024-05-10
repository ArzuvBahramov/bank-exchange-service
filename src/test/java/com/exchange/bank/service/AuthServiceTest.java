package com.exchange.bank.service;

import com.exchange.bank.dto.request.LoginRequest;
import com.exchange.bank.dto.request.LoginResponse;
import com.exchange.bank.mapper.UserMapper;
import com.exchange.bank.service.security.AuthDetailsService;
import com.exchange.bank.service.security.AuthService;
import com.exchange.bank.service.security.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static com.exchange.bank.utils.Constants.CLAIM_TOKEN_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig({AuthService.class})
public class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private AuthDetailsService userDetailsService;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;

    @Test
    public void testSignIn() {
        LoginRequest loginRequest = new LoginRequest("user", "password");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(loginRequest.username())).thenReturn(userDetails);
        when(tokenService.generateToken(userDetails)).thenReturn(CLAIM_TOKEN_NAME);

        LoginResponse result = authService.signIn(loginRequest);

        assertNotNull(result);
        assertEquals(CLAIM_TOKEN_NAME, result.jwttoken());
        verify(authenticationManager).authenticate(any());
        verify(userDetailsService).loadUserByUsername(loginRequest.username());
        verify(tokenService).generateToken(userDetails);
    }

    @Test
    public void testCreateRefreshToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Claims claims = Jwts.claims().setSubject("user123").build();
        when(request.getAttribute("claims")).thenReturn(claims);
        when(tokenService.doGenerateToken(anyMap(), eq("user123"))).thenReturn("newRefreshToken");

        LoginResponse result = authService.createRefreshToken(request);

        assertNotNull(result);
        assertEquals("newRefreshToken", result.jwttoken());
        verify(tokenService).doGenerateToken(anyMap(), eq("user123"));
    }
}
