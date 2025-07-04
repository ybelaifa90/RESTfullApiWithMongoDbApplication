package com.usmobile.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmobile.demo.core.BookDTO;
import com.usmobile.demo.entity.BookDAO;
import com.usmobile.demo.exception.DuplicateIsbnException;
import com.usmobile.demo.exception.EntityNotFoundException;
import com.usmobile.demo.exception.ErrorMessageEnum;
import com.usmobile.demo.exception.ServiceException;
import com.usmobile.demo.mapper.BookMapper;
import com.usmobile.demo.repository.BookRepositoryInterface;
import com.usmobile.demo.util.BookHelper;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service implementation for managing books.
 */
@Service
public class BookServiceImpl implements BookServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepositoryInterface bookRepository;
    private final BookMapper bookMapper;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new BookServiceImpl instance with the required dependencies.
     *
     * @param bookRepository the book repository interface
     * @param bookMapper     the book mapper instance
     * @param objectMapper   the object mapper instance
     */
    public BookServiceImpl(BookRepositoryInterface bookRepository, BookMapper bookMapper, ObjectMapper objectMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.objectMapper = objectMapper;
    }

    /**
     * Creates a new book.
     *
     * @param newBook the book to create
     * @return the created book
     * @throws DuplicateIsbnException if the ISBN already exists
     * @throws ServiceException       if an unexpected error occurs during creation
     */
    @Override
    public BookDTO createBook(BookDTO newBook) {
        try {
            logger.info("Creating new book with ISBN {}", newBook.getIsbn());

            BookDAO bookToPersist = bookMapper.toDAO(newBook);
            BookDAO savedBook = bookRepository.save(bookToPersist);

            logger.info("Book created successfully with ID {}", savedBook.getId());
            return bookMapper.toCore(savedBook);
        } catch (DataIntegrityViolationException e) {
            logger.error(ErrorMessageEnum.ISBN_ALREADY_EXISTS.getMessage(newBook.getIsbn()), e);
            throw new DuplicateIsbnException(ErrorMessageEnum.ISBN_ALREADY_EXISTS.getMessage(newBook.getIsbn()));
        } catch (Exception e) {
            logger.error(ErrorMessageEnum.UNEXPECTED_ERROR_OCCURRED.getMessage("creating book"), e);
            throw new ServiceException(ErrorMessageEnum.UNEXPECTED_ERROR_OCCURRED.getMessage("creating book"), e);
        }
    }

    /**
     * Updates an existing book.
     *
     * @param bookDTO the book to update
     * @return the updated book
     * @throws BadRequestException    if the update request is empty
     * @throws EntityNotFoundException if the book to update does not exist
     * @throws ServiceException       if an unexpected error occurs during update
     */
    @Override
    public BookDTO updateBook(BookDTO bookDTO) throws BadRequestException {
        if (isEmptyUpdate(bookDTO)) {
            logger.warn(ErrorMessageEnum.EMPTY_UPDATE_REQUEST.getMessage());
            throw new BadRequestException(ErrorMessageEnum.EMPTY_UPDATE_REQUEST.getMessage());
        }

        logger.info("Updating book with ID {}", bookDTO.getId());
        BookDAO existingBook = bookRepository.findById(bookDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessageEnum.BOOK_NOT_FOUND.getMessage(bookDTO.getId())));

        BookHelper.mergeNonNullWithValidation(bookDTO, existingBook);

        BookDAO updatedEntity = bookRepository.save(existingBook);
        logger.info("Book updated successfully with ID {}", updatedEntity.getId());
        return bookMapper.toCore(updatedEntity);
    }

    /**
     * Finds a book by ID.
     *
     * @param id the book ID
     * @return the book
     * @throws EntityNotFoundException if the book with the given ID does not exist
     */
    @Override
    public BookDTO findBookById(String id) {
        logger.info("Finding book with ID {}", id);
        return bookRepository.findById(id)
                .map(bookMapper::toCore)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessageEnum.BOOK_NOT_FOUND.getMessage(id)));
    }

    /**
     * Retrieves all books.
     *
     * @return the list of books
     */
    @Override
    public List<BookDTO> getAllBooks() {
        logger.info("Retrieving all books");
        return bookRepository.findAll().stream()
                .map(bookMapper::toCore)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a book by ID.
     *
     * @param id the book ID
     * @return true if deleted successfully
     * @throws EntityNotFoundException if the book with the given ID does not exist
     * @throws ServiceException       if an unexpected error occurs during deletion
     */
    public boolean deleteBook(String id) {
        try {
            logger.info("Deleting book with ID {}", id);
            BookDAO existingEntity = bookRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(ErrorMessageEnum.BOOK_NOT_FOUND.getMessage(id)));

            bookRepository.delete(existingEntity);

            logger.info("Book deleted successfully with ID {}", id);
            return true;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error(ErrorMessageEnum.BOOK_DELETION_ERROR.getMessage(id), e);
            throw new ServiceException(ErrorMessageEnum.BOOK_DELETION_ERROR.getMessage(id), e);
        }
    }

    /**
     * Checks if the update request contains any non-null fields (excluding the ID).
     * This prevents updates with no actual changes.
     * LocalDate empty string is considered null (Jackson can't deserialize an empty string to a LocalDate)
     *
     * @param bookDTO the book to update
     * @return true if the update request is empty (i.e., no non-null fields)
     */
    private boolean isEmptyUpdate(BookDTO bookDTO) {
        Map<String, Object> map = objectMapper.convertValue(bookDTO, Map.class);
        map.remove("id");
        map.values().removeIf(Objects::isNull);
        return map.isEmpty();
    }
}

