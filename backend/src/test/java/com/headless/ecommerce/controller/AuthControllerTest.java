package com.headless.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.headless.ecommerce.dto.request.LoginRequest;
import com.headless.ecommerce.dto.request.RegisterRequest;
import com.headless.ecommerce.dto.response.ApiResponse;
import com.headless.ecommerce.dto.response.JwtResponse;
import com.headless.ecommerce.exception.BusinessException;
import com.headless.ecommerce.security.JwtAuthenticationFilter;
import com.headless.ecommerce.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * WebMvcTest for AuthController.
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Nested
    @DisplayName("POST /api/v1/auth/register")
    class RegisterTests {

        @Test
        @DisplayName("Should register successfully and return JWT token")
        void register_success() throws Exception {
            RegisterRequest request = RegisterRequest.builder()
                .username("newuser")
                .password("password123")
                .nickname("New User")
                .build();

            JwtResponse jwtResponse = JwtResponse.of("jwt-token", 1L, "newuser", "BUYER");

            when(userService.register(any(RegisterRequest.class))).thenReturn(jwtResponse);

            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.token").value("jwt-token"))
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.username").value("newuser"))
                .andExpect(jsonPath("$.data.role").value("BUYER"));
        }

        @Test
        @DisplayName("Should return error when username already exists")
        void register_duplicateUsername() throws Exception {
            RegisterRequest request = RegisterRequest.builder()
                .username("existing")
                .password("password123")
                .build();

            when(userService.register(any(RegisterRequest.class)))
                .thenThrow(new BusinessException("Username already exists: existing"));

            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
        }

        @Test
        @DisplayName("Should return validation error for blank username")
        void register_blankUsername_validationError() throws Exception {
            RegisterRequest request = RegisterRequest.builder()
                .username("")
                .password("password123")
                .build();

            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/auth/login")
    class LoginTests {

        @Test
        @DisplayName("Should login successfully and return JWT token")
        void login_success() throws Exception {
            LoginRequest request = LoginRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

            JwtResponse jwtResponse = JwtResponse.of("jwt-token", 1L, "testuser", "BUYER");

            when(userService.login(any(LoginRequest.class))).thenReturn(jwtResponse);

            mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").value("jwt-token"));
        }

        @Test
        @DisplayName("Should return error for invalid credentials")
        void login_invalidCredentials() throws Exception {
            LoginRequest request = LoginRequest.builder()
                .username("testuser")
                .password("wrongpassword")
                .build();

            when(userService.login(any(LoginRequest.class)))
                .thenThrow(new BusinessException("Invalid username or password"));

            mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
        }
    }
}
