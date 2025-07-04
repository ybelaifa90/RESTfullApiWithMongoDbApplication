package com.usmobile.demo.exception;



/**
 * Exception thrown when a service-level error occurs.
 */
public class ServiceException extends RuntimeException {

    /**
     * Constructs a new ServiceException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause   the underlying cause of the exception
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}