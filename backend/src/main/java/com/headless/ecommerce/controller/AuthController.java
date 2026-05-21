package com.headless.ecommerce.controller;

import com.headless.ecommerce.dto.request.LoginRequest;
import com.headless.ecommerce.dto.request.RegisterRequest;
import com.headless.ecommerce.dto.response.ApiResponse;
import com.headless.ecommerce.dto.response.JwtResponse;
import com.headless.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication controller for user registration and login.
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "User registration and login APIs")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     *
     * @param request the registration request
     * @return the JWT response with token
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ApiResponse<JwtResponse> register(@Valid @RequestBody RegisterRequest request) {
        JwtResponse response = userService.register(request);
        return ApiResponse.success(response);
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param request the login request
     * @return the JWT response with token
     */
    @PostMapping("/login")
    @Operation(summary = "Login with username and password")
    public ApiResponse<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = userService.login(request);
        return ApiResponse.success(response);
    }
}
