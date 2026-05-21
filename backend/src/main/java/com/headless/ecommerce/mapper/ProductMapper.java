package com.headless.ecommerce.mapper;

import com.headless.ecommerce.dto.response.ProductResponse;
import com.headless.ecommerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Product entity ↔ ProductResponse DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Converts a Product entity to a ProductResponse DTO.
     *
     * @param product the Product entity
     * @return the ProductResponse DTO
     */
    @Mapping(target = "status", expression = "java(product.getStatus().name())")
    @Mapping(target = "categoryId", expression = "java(product.getCategory() != null ? product.getCategory().getId() : null)")
    @Mapping(target = "categoryName", expression = "java(product.getCategory() != null ? product.getCategory().getName() : null)")
    @Mapping(target = "createdAt", expression = "java(product.getCreatedAt() != null ? product.getCreatedAt().toString() : null)")
    @Mapping(target = "updatedAt", expression = "java(product.getUpdatedAt() != null ? product.getUpdatedAt().toString() : null)")
    ProductResponse toResponse(Product product);
}
