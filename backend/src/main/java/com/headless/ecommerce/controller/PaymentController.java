package com.headless.ecommerce.controller;

import com.headless.ecommerce.dto.response.ApiResponse;
import com.headless.ecommerce.dto.response.OrderResponse;
import com.headless.ecommerce.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * Payment controller for processing payments.
 */
@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments", description = "Payment processing APIs")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Processes a payment for the given order.
     *
     * @param orderNo the order number
     * @return the updated order response after payment
     */
    @PostMapping("/{orderNo}")
    @Operation(summary = "Process payment for an order")
    public ApiResponse<OrderResponse> processPayment(@PathVariable String orderNo) {
        OrderResponse response = paymentService.processPayment(orderNo);
        return ApiResponse.success(response);
    }
}
