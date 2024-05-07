package com.exchange.bank.services;


import com.exchange.bank.dao.entities.User;
import com.exchange.bank.dto.UserDto;
import com.exchange.bank.dto.request.RegisterRequest;
import com.exchange.bank.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class SignUpService {
    final PasswordEncoder passwordEncoder;
    final UserService userService;
    final UserMapper userMapper;

    public UserDto singUp(RegisterRequest request) {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        return userService.save(user);
    }
}
