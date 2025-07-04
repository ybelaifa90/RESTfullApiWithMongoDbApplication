package com.usmobile.demo.controller.v1;

import com.usmobile.demo.core.BookDTO;
import com.usmobile.demo.service.BookServiceInterface;
import com.usmobile.demo.util.ApiResponse;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing books.
 */
@RestController
@RequestMapping("/books")
public class BookController {

    /**
     * Service interface for book operations.
     */
    private final BookServiceInterface bookService;

    public BookController(BookServiceInterface bookService) {
        this.bookService = bookService;
    }

    /**
     * Creates a new book.
     *
     * @param bookDTO The book data to create.
     * @return A response entity with the created book data.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BookDTO>> createBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO savedDTO = bookService.createBook(bookDTO);
        ApiResponse<BookDTO> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Book created successfully",
                savedDTO
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves a list of all books.
     *
     * @return A response entity with a list of book data.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BookDTO>>> getAllBooks() {
        List<BookDTO> bookDTOList = bookService.getAllBooks();
        String message = bookDTOList.isEmpty() ? "No books found" : "Books found";
        ApiResponse<List<BookDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                message,
                bookDTOList
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id The ID of the book to retrieve.
     * @return A response entity with the book data.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDTO>> getBookById(@PathVariable String id) {
        BookDTO bookDTO = bookService.findBookById(id);
        ApiResponse<BookDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Book retrieved successfully",
                bookDTO
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id The ID of the book to delete.
     * @return A response entity with a boolean indicating whether the book was deleted.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteBook(@PathVariable String id) {
        boolean deleted = bookService.deleteBook(id);

        ApiResponse<Boolean> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Book deleted successfully",
                deleted
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Updates a book.
     *
     * @param bookDTO The book data to update.
     * @param id The ID of the book to update.
     * @return A response entity with the updated book data.
     * @throws BadRequestException If the request is invalid.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDTO>> updateBook(@RequestBody BookDTO bookDTO, @PathVariable String id) throws BadRequestException {
        bookDTO.setId(id);
        BookDTO updatedBook = bookService.updateBook(bookDTO);

        ApiResponse<BookDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Book updated successfully",
                updatedBook
        );

        return ResponseEntity.ok(response);
    }
}