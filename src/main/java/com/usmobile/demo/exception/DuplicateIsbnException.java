package com.usmobile.demo.exception;


/**
 * Exception thrown when a duplicate ISBN is encountered.
 */
public class DuplicateIsbnException extends RuntimeException {


    /**
     * Constructs a new DuplicateIsbnException with the specified message.
     *
     * @param message the detail message
     */
    public DuplicateIsbnException(String message) {
        super(message);
    }
}