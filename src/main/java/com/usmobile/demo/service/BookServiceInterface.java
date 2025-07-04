package com.usmobile.demo.service;


import com.usmobile.demo.core.BookDTO;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface BookServiceInterface {

    /**
     * Creates a new book.
     *
     * @param bookDTO the book object to create
     * @return the created book DTO
     */
    BookDTO createBook(BookDTO bookDTO);


    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book to retrieve
     * @return BookDTO
     */
    BookDTO findBookById(String id);

    /**
     * Retrieves a list of all books.
     *
     * @return a list of all book objects
     */

    List<BookDTO> getAllBooks();

    /**
     * Updates an existing book.
     *
     * @param bookDTO the book object to update
     * @return the updated book object
     * @throws BadRequestException if the update request is invalid or empty
     */

    BookDTO updateBook(BookDTO bookDTO) throws BadRequestException;


    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to delete
     * @return true if the book was deleted successfully, false otherwise
     */
    boolean deleteBook(String id);


}
