package com.headless.ecommerce.exception;

/**
 * Exception thrown when a user is not authorized to perform an action.
 */
public class UnauthorizedException extends RuntimeException {

    /**
     * Constructs an UnauthorizedException with a message.
     *
     * @param message the error message
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
