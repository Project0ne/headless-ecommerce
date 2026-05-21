package com.headless.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Unified API response wrapper.
 *
 * @param <T> the type of the response data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private int code;
    private String message;
    private T data;

    /**
     * Creates a success response with data.
     *
     * @param data the response data
     * @param <T> the data type
     * @return the ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    /**
     * Creates a success response with data and custom message.
     *
     * @param data the response data
     * @param message the success message
     * @param <T> the data type
     * @return the ApiResponse
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(200, message, data);
    }

    /**
     * Creates a success response without data.
     *
     * @param <T> the data type
     * @return the ApiResponse
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "success", null);
    }

    /**
     * Creates an error response with code and message.
     *
     * @param code the error code
     * @param message the error message
     * @param <T> the data type
     * @return the ApiResponse
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    /**
     * Creates an error response with code, message, and data.
     *
     * @param code the error code
     * @param message the error message
     * @param data additional error data
     * @param <T> the data type
     * @return the ApiResponse
     */
    public static <T> ApiResponse<T> error(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }
}
