package com.exchange.bank.controllers;

import com.exchange.bank.dto.UserDto;
import com.exchange.bank.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@SpringJUnitConfig({UserController.class})
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "testUser")
    public void testGetAllUsers() throws Exception {
        UserDto userDto = new UserDto(1L,
                "firstname",
                "lastname",
                "username",
                "email");
        Page<UserDto> result = new PageImpl<>(List.of(userDto));
        when(userService.getAllUsers(any())).thenReturn(result);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testGetUserByUsername() throws Exception {
        UserDto userDto = new UserDto(1L,
                "firstname",
                "lastname",
                "test",
                "email");
        when(userService.getUserByUsername("test")).thenReturn(userDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/{username}", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test"));
    }
}
