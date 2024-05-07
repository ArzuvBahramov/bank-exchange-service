package com.exchange.bank.services;

import com.exchange.bank.dao.entities.User;
import com.exchange.bank.dto.UserDto;
import com.exchange.bank.dto.request.RegisterRequest;
import com.exchange.bank.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig({SignUpService.class})
public class SignUpServiceTest {
    @Autowired
    private SignUpService signUpService;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;

    @Test
    public void testSignUp() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "johndoe", "johndoe@example.com", "password123");
        User user = new User();
        UserDto userDto = new UserDto(1L, "John", "Doe", "johndoe", "johndoe@example.com");

        when(userMapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userService.save(any(User.class))).thenReturn(userDto);

        UserDto result = signUpService.singUp(request);

        assertNotNull(result);
        assertEquals("John", result.firstname());
        assertEquals("Doe", result.lastname());
        assertEquals("johndoe", result.username());
        assertEquals("johndoe@example.com", result.email());

        verify(userMapper).toEntity(request);
        verify(passwordEncoder).encode(request.password());
        verify(userService).save(user);
    }
}
