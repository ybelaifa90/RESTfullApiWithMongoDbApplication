package com.usmobile.demo.repository;

import com.usmobile.demo.core.BookDTO;
import com.usmobile.demo.entity.BookDAO;
import com.usmobile.demo.mapper.BookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;



/**
 * Initializes the database with sample data when the application starts.
 * Deletes all existing data and inserts new sample books.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final BookRepositoryInterface bookRepository;
    private final BookMapper bookMapper;

    /**
     * Constructs a new DataInitializer instance with the required dependencies.
     *
     * @param bookRepository the book repository interface
     * @param bookMapper     the book mapper instance
     */
    @Autowired
    public DataInitializer(BookRepositoryInterface bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    /**
     * Initializes the database with sample data.
     *
     * @param args command-line arguments (not used as data initialization is automatic)
     */
    @Override
    public void run(String... args) {
        logger.info("Initializing database with sample data...");

        try {
            bookRepository.deleteAll();

            BookDTO book1 = new BookDTO();
            book1.setTitle("Book 1");
            book1.setAuthor("Author 1");
            book1.setIsbn("1234567890");
            book1.setPublishedDate(LocalDate.of(2025, 7, 1));

            BookDTO book2 = new BookDTO();
            book2.setTitle("Book 2");
            book2.setAuthor("Author 2");
            book2.setIsbn("1234567888");
            book2.setPublishedDate(LocalDate.of(2025, 6, 29));

            List<BookDAO> books = Arrays.asList(
                    bookMapper.toDAO(book1),
                    bookMapper.toDAO(book2)
            );

            bookRepository.saveAll(books);
            logger.info("Sample data initialization complete.");
        } catch (Exception e) {
            logger.error("Error initializing sample data", e);
        }
    }
}