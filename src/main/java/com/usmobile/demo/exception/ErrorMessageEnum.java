package com.usmobile.demo.exception;


/**
 * ErrorMessageEnum for error messages
 */
public enum ErrorMessageEnum {
    BOOK_NOT_FOUND("Book with ID %s not found"),
    ISBN_ALREADY_EXISTS("ISBN '%s' already exists"),
    EMPTY_UPDATE_REQUEST("At least one field must be provided for update. Null values not accepted"),
    UNEXPECTED_ERROR_OCCURRED("Unexpected error occurred while %s"),
    INVALID_BOOK_DATA("Invalid book data: %s"),
    BOOK_DELETION_ERROR("Error deleting book with ID %s");

    private final String message;

    ErrorMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}