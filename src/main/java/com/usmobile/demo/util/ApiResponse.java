package com.usmobile.demo.util;


import lombok.Getter;
import lombok.Setter;



/**
 * A generic API response class that encapsulates the response status, message, data, and error code.
 *
 * @param <T> the type of data in the response
 */
@Getter
@Setter
public class ApiResponse<T> {
    /**
     * The HTTP status code of the response.
     */
    private int statusCode;

    /**
     * A message describing the response.
     */
    private String message;

    /**
     * The data returned in the response.
     */
    private T data;

    /**
     * An error code for error responses.
     */
    private String errorCode;

    /**
     * Constructs a successful API response with the given status code, message, and data.
     *
     * @param statusCode the HTTP status code
     * @param message    a message describing the response
     * @param data       the data returned in the response
     */
    public ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    /**
     * Constructs an error API response with the given status code, message, and error code.
     *
     * @param statusCode the HTTP status code
     * @param message    a message describing the error
     * @param errorCode  an error code for the response
     */
    public ApiResponse(int statusCode, String message, String errorCode) {
        this.statusCode = statusCode;
        this.message = message;
        this.errorCode = errorCode;
    }
}