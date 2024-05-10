package com.exchange.bank.service;

import com.exchange.bank.dao.entity.User;
import com.exchange.bank.dao.repository.UserRepository;
import com.exchange.bank.dto.UserDto;
import com.exchange.bank.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig({UserService.class})
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    UserRepository userRepository;
    @MockBean
    UserMapper userMapper;

    @Test
    public void testSave() {
        User user = new User();
        user.setId(1L);
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setUsername("johndoe");
        user.setEmail("john.doe@example.com");

        UserDto userDto = new UserDto(1L, "John", "Doe", "johndoe", "john.doe@example.com");

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto result = userService.save(new User());

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("John", result.firstname());
        assertEquals("Doe", result.lastname());
        assertEquals("johndoe", result.username());
        assertEquals("john.doe@example.com", result.email());

        verify(userRepository).save(any(User.class));
        verify(userMapper).toDto(any(User.class));
    }

    @Test
    public void testGetUserByUsername() {
        String username = "testUser";
        Optional<User> optionalUser = Optional.of(new User());

        when(userRepository.findByUsername(username)).thenReturn(optionalUser);

        Optional<User> result = userService.getUserDetailsByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(optionalUser, result);
        verify(userRepository).findByUsername(username);
    }
}
