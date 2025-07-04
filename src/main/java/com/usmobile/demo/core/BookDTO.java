package com.usmobile.demo.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * class: Book
 */

@Data
public class BookDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    @NotBlank(message = "Title is required and cannot be empty")
    private String title;

    @NotBlank(message = "author is required and cannot be empty")
    private String author;

    @NotNull(message = "isbn is required")
    @Size(min = 10, max = 13, message = "ISBN must be either 10 or 13 characters long")
    private String isbn;

    @NotNull(message = "publishedDate is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate publishedDate;

}
