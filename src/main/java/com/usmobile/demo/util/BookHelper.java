package com.usmobile.demo.util;

import com.usmobile.demo.core.BookDTO;
import com.usmobile.demo.entity.BookDAO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * A utility class that provides helper methods for working with books.
 */
@Component
public class BookHelper {

    /**
     * The validator used to validate book fields.
     */
    private static Validator validator;

    /**
     * Constructs a new BookHelper instance with the given validator.
     *
     * @param validator the validator to use
     */
    @Autowired
    public BookHelper(Validator validator) {
        BookHelper.validator = validator;
    }

    /**
     * Merges non-null fields from the given BookDTO into the target BookDAO, validating each field before merging.
     *
     * @param source the BookDTO to merge from
     * @param target the BookDAO to merge into
     */
    public static void mergeNonNullWithValidation(BookDTO source, BookDAO target) {
        if (source.getTitle() != null) {
            validateField(source, "title");
            target.setTitle(source.getTitle());
        }
        if (source.getAuthor() != null) {
            validateField(source, "author");
            target.setAuthor(source.getAuthor());
        }
        if (source.getIsbn() != null) {
            validateField(source, "isbn");
            target.setIsbn(source.getIsbn());
        }
        if (source.getPublishedDate() != null) {
            validateField(source, "publishedDate");
            target.setPublishedDate(source.getPublishedDate());
        }
    }

    /**
     * Validates the specified field of the given object using the validator.
     *
     * @param obj         the object to validate
     * @param propertyName the name of the field to validate
     * @throws ConstraintViolationException if the field is invalid
     */
    private static void validateField(Object obj, String propertyName) {
        Set<ConstraintViolation<Object>> violations = validator.validateProperty(obj, propertyName);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}