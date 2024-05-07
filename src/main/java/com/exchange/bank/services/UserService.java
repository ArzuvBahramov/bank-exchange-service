package com.exchange.bank.services;

import com.exchange.bank.dao.entities.User;
import com.exchange.bank.dao.repositories.UserRepository;
import com.exchange.bank.dto.UserDto;
import com.exchange.bank.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class UserService {
    final UserRepository userRepository;
    final UserMapper userMapper;

    public UserDto save(User user) {
        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
