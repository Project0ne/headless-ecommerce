package com.headless.ecommerce.mapper;

import com.headless.ecommerce.dto.response.CategoryResponse;
import com.headless.ecommerce.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * MapStruct mapper for Category entity ↔ CategoryResponse DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * Converts a Category entity to a CategoryResponse DTO.
     *
     * @param category the Category entity
     * @return the CategoryResponse DTO
     */
    @Mapping(target = "parentId", expression = "java(category.getParent() != null ? category.getParent().getId() : null)")
    @Mapping(target = "createdAt", expression = "java(category.getCreatedAt() != null ? category.getCreatedAt().toString() : null)")
    CategoryResponse toResponse(Category category);

    /**
     * Converts a list of Category entities to a list of CategoryResponse DTOs.
     *
     * @param categories the list of Category entities
     * @return the list of CategoryResponse DTOs
     */
    List<CategoryResponse> toResponseList(List<Category> categories);
}
