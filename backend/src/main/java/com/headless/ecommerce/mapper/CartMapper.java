package com.headless.ecommerce.mapper;

import com.headless.ecommerce.dto.response.CartItemResponse;
import com.headless.ecommerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for cart item conversion from Product entity to CartItemResponse DTO.
 */
@Mapper(componentModel = "spring")
public interface CartMapper {

    /**
     * Converts a Product entity to a CartItemResponse DTO.
     *
     * @param product the Product entity
     * @param quantity the cart item quantity
     * @return the CartItemResponse DTO
     */
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productImage", source = "product.imageUrl")
    @Mapping(target = "unitPrice", source = "product.price")
    @Mapping(target = "available", expression = "java(product.getStatus().name().equals(\"ON_SHELF\") && product.getStock() > 0)")
    @Mapping(target = "stock", source = "product.stock")
    CartItemResponse toCartItemResponse(Product product, Integer quantity);
}
