package com.exchange.bank.services;

import com.exchange.bank.dao.entities.User;
import com.exchange.bank.dao.repositories.UserRepository;
import com.exchange.bank.dto.UserDto;
import com.exchange.bank.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
@Service
public class UserService {
    final UserRepository userRepository;
    final UserMapper userMapper;

    public UserDto save(User user) {
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
