package com.headless.ecommerce.mapper;

import com.headless.ecommerce.dto.response.OrderItemResponse;
import com.headless.ecommerce.dto.response.OrderResponse;
import com.headless.ecommerce.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * MapStruct mapper for Order entity ↔ OrderResponse DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

    /**
     * Converts an Order entity to an OrderResponse DTO.
     *
     * @param order the Order entity
     * @return the OrderResponse DTO
     */
    @Mapping(target = "status", expression = "java(order.getStatus().name())")
    @Mapping(target = "paidAt", expression = "java(order.getPaidAt() != null ? order.getPaidAt().toString() : null)")
    @Mapping(target = "createdAt", expression = "java(order.getCreatedAt() != null ? order.getCreatedAt().toString() : null)")
    @Mapping(target = "updatedAt", expression = "java(order.getUpdatedAt() != null ? order.getUpdatedAt().toString() : null)")
    @Mapping(target = "orderItems", source = "orderItems")
    OrderResponse toResponse(Order order);

    /**
     * Converts an OrderItem entity to an OrderItemResponse DTO.
     *
     * @param orderItem the OrderItem entity
     * @return the OrderItemResponse DTO
     */
    OrderItemResponse toItemResponse(OrderItem orderItem);

    /**
     * Converts a list of OrderItem entities to OrderItemResponse DTOs.
     *
     * @param orderItems the list of OrderItem entities
     * @return the list of OrderItemResponse DTOs
     */
    List<OrderItemResponse> toItemResponseList(List<com.headless.ecommerce.model.OrderItem> orderItems);
}
