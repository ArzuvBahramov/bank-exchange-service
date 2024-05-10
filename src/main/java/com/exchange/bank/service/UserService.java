package com.exchange.bank.service;

import com.exchange.bank.dao.entity.User;
import com.exchange.bank.dao.repository.UserRepository;
import com.exchange.bank.dto.UserDto;
import com.exchange.bank.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Optional<User> getUserDetailsByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return userMapper.toDto(user);
    }
}
