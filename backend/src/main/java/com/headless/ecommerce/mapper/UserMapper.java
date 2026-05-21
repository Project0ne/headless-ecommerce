package com.headless.ecommerce.mapper;

import com.headless.ecommerce.dto.response.UserResponse;
import com.headless.ecommerce.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for User entity ↔ UserResponse DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts a User entity to a UserResponse DTO.
     *
     * @param user the User entity
     * @return the UserResponse DTO
     */
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    @Mapping(target = "createdAt", expression = "java(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null)")
    UserResponse toResponse(User user);
}
