package com.headless.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Order creation request DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateRequest {

    @NotBlank(message = "Receiver name is required")
    @Size(max = 50, message = "Receiver name must be at most 50 characters")
    private String receiverName;

    @NotBlank(message = "Receiver phone is required")
    @Size(max = 20, message = "Receiver phone must be at most 20 characters")
    private String receiverPhone;

    @NotBlank(message = "Receiver address is required")
    @Size(max = 255, message = "Receiver address must be at most 255 characters")
    private String receiverAddress;
}
