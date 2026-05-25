package com.headless.ecommerce.dto.request;

import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating the current user's profile.
 * Only allows updating safe fields — no role, id, or username changes.
 */
public record UserUpdateRequest(
    @Size(max = 50, message = "Nickname must be at most 50 characters")
    String nickname,

    String avatar,

    @Size(max = 20, message = "Phone must be at most 20 characters")
    String phone,

    @Size(max = 200, message = "Address must be at most 200 characters")
    String address
) {}
