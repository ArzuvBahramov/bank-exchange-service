package com.exchange.bank.controllers;

import com.exchange.bank.dto.UserDto;
import com.exchange.bank.dto.request.LoginRequest;
import com.exchange.bank.dto.request.LoginResponse;
import com.exchange.bank.dto.request.RegisterRequest;
import com.exchange.bank.services.AuthService;
import com.exchange.bank.services.SignUpService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@SpringJUnitConfig(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;
    @MockBean
    private SignUpService signUpService;

    @Test
    @WithMockUser
    public void testSignIn() throws Exception {
        String loginRequestJson = "{\"username\":\"testUser\",\"password\":\"password123\"}";
        LoginResponse response = new LoginResponse("jwttoken");
        when(authService.signIn(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/v1/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwttoken").value("jwttoken"));
    }

    @Test
    @WithMockUser
    public void testSignUp() throws Exception {
        String registerRequestJson = "{\"firstname\":\"John\",\"lastname\":\"Doe\",\"username\":\"johndoe\",\"email\":\"john.doe@example.com\",\"password\":\"password123\"}";
        UserDto userDto = new UserDto(1L, "John", "Doe", "johndoe", "john.doe@example.com");
        when(signUpService.singUp(any(RegisterRequest.class))).thenReturn(userDto);
        mockMvc.perform(post("/v1/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequestJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testCreateRefreshToken() throws Exception {
        LoginResponse response = new LoginResponse("jwttoken");
        when(authService.createRefreshToken(any(HttpServletRequest.class))).thenReturn(response);
        mockMvc.perform(get("/v1/auth/refresh-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwttoken").value("jwttoken"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testGetDetails() throws Exception {
        mockMvc.perform(get("/v1/auth/api/details"))
                .andExpect(status().isOk())
                .andExpect(content().string("SUCCESS"));
    }
}
