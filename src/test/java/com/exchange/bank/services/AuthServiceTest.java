package com.exchange.bank.services;

import com.exchange.bank.dto.request.LoginRequest;
import com.exchange.bank.dto.request.LoginResponse;
import com.exchange.bank.security.service.AuthDetailsService;
import com.exchange.bank.security.util.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

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
    private TokenUtil tokenUtil;
    @MockBean
    private AuthDetailsService userDetailsService;

    @Test
    public void testSignIn() {
        LoginRequest loginRequest = new LoginRequest("user", "password");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(loginRequest.username())).thenReturn(userDetails);
        when(tokenUtil.generateToken(userDetails)).thenReturn("token");

        LoginResponse result = authService.signIn(loginRequest);

        assertNotNull(result);
        assertEquals("token", result.jwttoken());
        verify(authenticationManager).authenticate(any());
        verify(userDetailsService).loadUserByUsername(loginRequest.username());
        verify(tokenUtil).generateToken(userDetails);
    }

    @Test
    public void testCreateRefreshToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Claims claims = Jwts.claims().setSubject("user123").build();
        when(request.getAttribute("claims")).thenReturn(claims);
        when(tokenUtil.doGenerateToken(anyMap(), eq("user123"))).thenReturn("newRefreshToken");

        LoginResponse result = authService.createRefreshToken(request);

        assertNotNull(result);
        assertEquals("newRefreshToken", result.jwttoken());
        verify(tokenUtil).doGenerateToken(anyMap(), eq("user123"));
    }
}
