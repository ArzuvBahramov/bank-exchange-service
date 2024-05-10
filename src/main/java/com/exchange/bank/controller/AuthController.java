package com.exchange.bank.controller;

import com.exchange.bank.dto.UserDto;
import com.exchange.bank.dto.request.LoginRequest;
import com.exchange.bank.dto.request.LoginResponse;
import com.exchange.bank.dto.request.RegisterRequest;
import com.exchange.bank.service.security.AuthService;
import com.exchange.bank.service.SignUpService;
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
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    final AuthService authService;
    final SignUpService signUpService;

    @PostMapping("/sign-in")
    @SneakyThrows
    public ResponseEntity<LoginResponse> signIn(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.signIn(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUp(@RequestBody RegisterRequest request) {
        UserDto user = signUpService.singUp(request);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> createRefreshToken(HttpServletRequest request) {
        LoginResponse response = authService.createRefreshToken(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/api/details")
    public ResponseEntity<?> getDetails() {
        UserDto userDetails = authService.getUserDetails();
        return ResponseEntity.ok(userDetails);
    }

}
