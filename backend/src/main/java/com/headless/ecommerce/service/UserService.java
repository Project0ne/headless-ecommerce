package com.headless.ecommerce.service;

import com.headless.ecommerce.dto.request.LoginRequest;
import com.headless.ecommerce.dto.request.RegisterRequest;
import com.headless.ecommerce.dto.request.UserUpdateRequest;
import com.headless.ecommerce.dto.response.JwtResponse;
import com.headless.ecommerce.dto.response.UserResponse;
import com.headless.ecommerce.model.User;

import java.util.Optional;

/**
 * User service interface for authentication and user management.
 */
public interface UserService {

    /**
     * Registers a new user and returns a JWT response.
     *
     * @param request the registration request
     * @return the JWT response with token and user info
     */
    JwtResponse register(RegisterRequest request);

    /**
     * Authenticates a user and returns a JWT response.
     *
     * @param request the login request
     * @return the JWT response with token and user info
     */
    JwtResponse login(LoginRequest request);

    /**
     * Retrieves the current user's profile.
     *
     * @param userId the user ID
     * @return the user response DTO
     */
    UserResponse getCurrentUser(Long userId);

    /**
     * Updates the current user's profile.
     *
     * @param userId the user ID
     * @param request the updated user data (safe fields only)
     * @return the updated user response DTO
     */
    UserResponse updateCurrentUser(Long userId, UserUpdateRequest request);

    /**
     * Finds a user entity by ID.
     *
     * @param userId the user ID
     * @return the User entity
     */
    User findById(Long userId);

    /**
     * Finds a user entity by username.
     *
     * @param username the username
     * @return an Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
}
