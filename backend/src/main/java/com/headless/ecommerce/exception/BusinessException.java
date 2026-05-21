package com.headless.ecommerce.exception;

/**
 * Business exception for domain-specific error conditions.
 */
public class BusinessException extends RuntimeException {

    private final int code;

    /**
     * Constructs a BusinessException with message and default code 400.
     *
     * @param message the error message
     */
    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    /**
     * Constructs a BusinessException with code and message.
     *
     * @param code the business error code
     * @param message the error message
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * Returns the business error code.
     *
     * @return the error code
     */
    public int getCode() {
        return code;
    }
}
