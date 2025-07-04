package com.usmobile.demo.repository;

import com.usmobile.demo.entity.BookDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class BookRepositoryTest {

    @Autowired
    private BookRepositoryInterface bookRepository;

    private BookDAO book;

    @BeforeEach
    void setup() {
        book = bookRepository.save(getBookDAOTestObj());
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void testCreateBook() {

        BookDAO bookDAO = getBookDAOTestObj();
        // setting a new isnb to avoid duplicate key exception because @before each already creates a book
        bookDAO.setIsbn("5555555555");
        BookDAO savedBook = bookRepository.save(bookDAO);

        // Assertions
        assertNotNull(savedBook);
        assertNotNull(savedBook.getId());
        assertEquals("Test Book", savedBook.getTitle());
    }

    @Test
    void testGetBookById() {
        Optional<BookDAO> foundBook = bookRepository.findById(book.getId());

        // Assertions
        assertTrue(foundBook.isPresent());
        assertEquals("Test Book", foundBook.get().getTitle());
    }

    @Test
    void testDeleteBook() {
        bookRepository.deleteById(book.getId());

        Optional<BookDAO> foundBook = bookRepository.findById(book.getId());
        // Assertions
        assertFalse(foundBook.isPresent());
    }

    @Test
    void testFindAllBooks() {
        List<BookDAO> books = bookRepository.findAll();

        // Assertions
        assertNotNull(books);
        assertTrue(books.contains(book));
    }

    private BookDAO getBookDAOTestObj() {
        BookDAO book = new BookDAO();
        book.setTitle("Test Book");
        book.setAuthor("Author A");
        book.setIsbn("1234567890123");
        book.setPublishedDate(LocalDate.of(2024, 1, 1));
        return book;
    }
}