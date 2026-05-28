package com.headless.ecommerce.mapper;

import com.headless.ecommerce.dto.response.ProductImageResponse;
import com.headless.ecommerce.dto.response.ProductResponse;
import com.headless.ecommerce.model.Product;
import com.headless.ecommerce.model.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

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
    @Mapping(target = "images", expression = "java(mapImages(product.getImages()))")
    ProductResponse toResponse(Product product);

    /**
     * Maps ProductImage list to ProductImageResponse list.
     */
    default List<ProductImageResponse> mapImages(List<ProductImage> images) {
        if (images == null) return List.of();
        return images.stream()
            .map(img -> ProductImageResponse.builder()
                .id(img.getId())
                .imageUrl(img.getImageUrl())
                .sortOrder(img.getSortOrder())
                .isPrimary(img.getIsPrimary())
                .build())
            .collect(Collectors.toList());
    }
}
