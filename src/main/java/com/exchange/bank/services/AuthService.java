package com.exchange.bank.services;

import com.exchange.bank.dao.entities.User;
import com.exchange.bank.dto.UserDto;
import com.exchange.bank.dto.request.LoginRequest;
import com.exchange.bank.dto.request.LoginResponse;
import com.exchange.bank.mapper.UserMapper;
import com.exchange.bank.security.service.AuthDetailsService;
import com.exchange.bank.security.service.TokenService;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

import static com.exchange.bank.config.Constants.CLAIM_ATTRIBUTE_NAME;

@Slf4j
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class AuthService {
    final AuthenticationManager authenticationManager;
    final TokenService tokenService;
    final AuthDetailsService userDetailsService;
    final UserService userService;
    final UserMapper userMapper;

    @SneakyThrows
    public LoginResponse signIn(@RequestBody LoginRequest loginRequest) {
        authenticate(loginRequest.username(), loginRequest.password());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(loginRequest.username());

        final String token = tokenService.generateToken(userDetails);

        log.info("User {} is successfully authenticated.", loginRequest.username());
        return new LoginResponse(token);
    }

    public LoginResponse createRefreshToken(HttpServletRequest request) {
        DefaultClaims claims = (DefaultClaims) request.getAttribute(CLAIM_ATTRIBUTE_NAME);
        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
        String token = tokenService.doGenerateToken(expectedMap, expectedMap.get("sub").toString());

        return new LoginResponse(token);
    }

    public UserDto getUserDetails() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User user = userService.getUserDetailsByUsername(username).orElseThrow();
        return userMapper.toDto(user);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            log.error("User: {} disabled", username);
            throw new RuntimeException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials username: {}, password: {}.", username, password);
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    private Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        return new HashMap<>(claims);
    }
}
