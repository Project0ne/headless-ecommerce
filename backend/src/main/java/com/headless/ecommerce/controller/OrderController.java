package com.headless.ecommerce.controller;

import com.headless.ecommerce.dto.request.OrderCreateRequest;
import com.headless.ecommerce.dto.response.ApiResponse;
import com.headless.ecommerce.dto.response.OrderResponse;
import com.headless.ecommerce.dto.response.PageResponse;
import com.headless.ecommerce.service.OrderService;
import com.headless.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Order controller for order management.
 */
@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Order management APIs")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    /**
     * Creates a new order from the user's cart.
     *
     * @param userDetails the authenticated user
     * @param request the order creation request
     * @return the created order response
     */
    @PostMapping
    @Operation(summary = "Create a new order")
    public ApiResponse<OrderResponse> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OrderCreateRequest request) {
        Long userId = getUserId(userDetails);
        OrderResponse response = orderService.createOrder(userId, request);
        return ApiResponse.success(response);
    }

    /**
     * Gets a paginated list of the user's orders.
     *
     * @param userDetails the authenticated user
     * @param page page number
     * @param size page size
     * @return paginated order response
     */
    @GetMapping
    @Operation(summary = "Get user's order list")
    public ApiResponse<PageResponse<OrderResponse>> getUserOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Long userId = getUserId(userDetails);
        PageResponse<OrderResponse> response = orderService.getUserOrders(userId, page, size);
        return ApiResponse.success(response);
    }

    /**
     * Gets an order by ID.
     *
     * @param userDetails the authenticated user
     * @param id the order ID
     * @return the order response
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get order detail by ID")
    public ApiResponse<OrderResponse> getOrderById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        Long userId = getUserId(userDetails);
        OrderResponse response = orderService.getOrderById(id, userId);
        return ApiResponse.success(response);
    }

    /**
     * Cancels an order.
     *
     * @param userDetails the authenticated user
     * @param id the order ID
     * @return the cancelled order response
     */
    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel an order")
    public ApiResponse<OrderResponse> cancelOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        Long userId = getUserId(userDetails);
        OrderResponse response = orderService.cancelOrder(id, userId);
        return ApiResponse.success(response);
    }

    /**
     * Extracts the user ID from authenticated user details.
     */
    private Long getUserId(UserDetails userDetails) {
        return userService.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new com.headless.ecommerce.exception.ResourceNotFoundException(
                "User", "username", userDetails.getUsername()))
            .getId();
    }
}
