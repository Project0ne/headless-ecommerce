package com.headless.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT authentication response containing the token and user info.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String token;
    private String tokenType;
    private Long userId;
    private String username;
    private String role;

    /**
     * Creates a JwtResponse with token and user details.
     *
     * @param token the JWT token
     * @param userId the user ID
     * @param username the username
     * @param role the user role
     * @return the JwtResponse
     */
    public static JwtResponse of(String token, Long userId, String username, String role) {
        return new JwtResponse(token, "Bearer", userId, username, role);
    }
}
