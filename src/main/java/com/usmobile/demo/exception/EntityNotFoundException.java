package com.usmobile.demo.exception;



/**
 * Exception thrown when an entity is not found.
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Constructs a new EntityNotFoundException with the specified message.
     *
     * @param message the detail message
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
