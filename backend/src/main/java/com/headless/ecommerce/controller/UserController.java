package com.headless.ecommerce.controller;

import com.headless.ecommerce.dto.request.UserUpdateRequest;
import com.headless.ecommerce.dto.response.ApiResponse;
import com.headless.ecommerce.dto.response.UserResponse;
import com.headless.ecommerce.exception.ResourceNotFoundException;
import com.headless.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * User controller for user profile management.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "User profile APIs")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves the current authenticated user's profile.
     *
     * @param userDetails the authenticated user details
     * @return the user profile response
     */
    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ApiResponse<UserResponse> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = extractUserId(userDetails);
        UserResponse response = userService.getCurrentUser(userId);
        return ApiResponse.success(response);
    }

    /**
     * Updates the current authenticated user's profile.
     *
     * @param userDetails the authenticated user details
     * @param request the updated user data (safe fields only)
     * @return the updated user profile response
     */
    @PutMapping("/me")
    @Operation(summary = "Update current user profile")
    public ApiResponse<UserResponse> updateCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserUpdateRequest request) {
        Long userId = extractUserId(userDetails);
        UserResponse response = userService.updateCurrentUser(userId, request);
        return ApiResponse.success(response);
    }

    /**
     * Extracts the user ID from the JWT authentication principal.
     *
     * @param userDetails the authenticated user details
     * @return the user ID
     */
    private Long extractUserId(UserDetails userDetails) {
        return userService.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException(
                "User", "username", userDetails.getUsername()))
            .getId();
    }
}
