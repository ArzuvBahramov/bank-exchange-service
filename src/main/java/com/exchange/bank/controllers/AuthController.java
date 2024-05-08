package com.exchange.bank.controllers;

import com.exchange.bank.dto.UserDto;
import com.exchange.bank.dto.request.LoginRequest;
import com.exchange.bank.dto.request.LoginResponse;
import com.exchange.bank.dto.request.RegisterRequest;
import com.exchange.bank.services.AuthService;
import com.exchange.bank.services.SignUpService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(maxAge = 3600)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    final AuthService authService;
    final SignUpService signUpService;

    @PostMapping("/sign-in")
    @SneakyThrows
    public ResponseEntity<LoginResponse> signIn(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.signIn(loginRequest));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUp(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(signUpService.singUp(request));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> createRefreshToken(HttpServletRequest request) {
        return ResponseEntity.ok(authService.createRefreshToken(request));
    }

    @GetMapping(value = "/api/details")
    public ResponseEntity<?> getDetails() {
        return ResponseEntity.ok(authService.getUserDetails());
    }

}
