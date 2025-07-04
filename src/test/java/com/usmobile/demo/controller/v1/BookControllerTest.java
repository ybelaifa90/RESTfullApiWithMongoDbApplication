package com.usmobile.demo.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmobile.demo.core.BookDTO;
import com.usmobile.demo.exception.DuplicateIsbnException;
import com.usmobile.demo.exception.EntityNotFoundException;
import com.usmobile.demo.exception.ServiceException;
import com.usmobile.demo.service.BookServiceInterface;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookServiceInterface bookService;

    @Test
    void testCreateBook_success() throws Exception {
        Mockito.when(bookService.createBook(any(BookDTO.class))).thenReturn(getBookDTOTestObj());

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBookDTOTestObj())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value("Test Book"))
                .andExpect(jsonPath("$.data.isbn").value("1234567890123"));
    }

    @Test
    void testCreateBook_missingTitle() throws Exception {

        BookDTO bookDTO = getBookDTOTestObj();
        bookDTO.setTitle("");

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Title is required and cannot be empty"))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    void testCreateBook_duplicateKeyException() throws Exception {
        Mockito.when(bookService.createBook(any(BookDTO.class)))
                .thenThrow(new DuplicateIsbnException("ISBN '1234567890123' already exists"));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBookDTOTestObj())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ISBN '1234567890123' already exists"))
                .andExpect(jsonPath("$.errorCode").value("DUPLICATE_ISBN"));
    }

    @Test
    void testCreateCreationErrorException_unexpectedError() throws Exception {
        Mockito.when(bookService.createBook(any(BookDTO.class)))
                .thenThrow(new ServiceException("Unexpected error occurred while creating book", new RuntimeException()));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBookDTOTestObj())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Unexpected error occurred while creating book"));
    }


    // GET ALL

    @Test
    void testGetAllBooks_returnsBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(getBookDTOTestObj()));

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data[0].title").value("Test Book"));
    }

    @Test
    void testGetAllBooks_returnsEmptyList() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of());

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No books found"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    // DELETE
    @Test
    void testDeleteBook_success() throws Exception {
        String bookId = "123AXXDx";

        when(bookService.deleteBook(bookId)).thenReturn(true);

        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book deleted successfully"))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void testDeleteBook_entityNotFoundException() throws Exception {
        String bookId = "456axxs";
        doThrow(new EntityNotFoundException("Not found")).when(bookService).deleteBook(bookId);

        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBookDTOTestObj())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not found"))
                .andExpect(jsonPath("$.errorCode").value("ENTITY_NOT_FOUND"));
    }

    @Test
    void testDeleteBook_unexpectedError() throws Exception {
        String bookId = "789";

        when(bookService.deleteBook(bookId))
                .thenThrow(new ServiceException("Unexpected error occurred while deleting book with id " + bookId, new RuntimeException()));

        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Unexpected error occurred while deleting book with id " + bookId));
    }

    // GET BY ID
    @Test
    void testGetBookById_found() throws Exception {
        BookDTO bookDTO = getBookDTOTestObj();
        Mockito.when(bookService.findBookById("ABC1xs")).thenReturn(bookDTO);

        mockMvc.perform(get("/books/{id}", "ABC1xs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(bookDTO.getId()))
                .andExpect(jsonPath("$.data.title").value(bookDTO.getTitle()));
    }


    @Test
    void testGetBookById_entityNotFoundException() throws Exception {
        Mockito.when(bookService.findBookById(anyString()))
                .thenThrow(new EntityNotFoundException("Book with ID 1 not found"));

        mockMvc.perform(get("/books/{id}", "1"))
                .andExpect(status().isNotFound());
    }

    // UPDATE

    @Test
    void testUpdateBook_success() throws Exception {
        BookDTO bookDTO = getBookDTOTestObj();
        bookDTO.setId("1234567");
        Mockito.when(bookService.updateBook(any(BookDTO.class))).thenReturn(bookDTO);

        mockMvc.perform(put("/books/{id}", bookDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book updated successfully"))
                .andExpect(jsonPath("$.data.id").value(bookDTO.getId()))
                .andExpect(jsonPath("$.data.title").value(bookDTO.getTitle()));
    }


    @Test
    void testUpdateBook_invalidIsbn() throws Exception {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId("1234567");
        bookDTO.setIsbn("123");

        Mockito.when(bookService.updateBook(any(BookDTO.class)))
                .thenThrow(new BadRequestException("ISBN must be either 10 or 13 characters long"));

        mockMvc.perform(put("/books/{id}", bookDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ISBN must be either 10 or 13 characters long"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }


    @Test
    void testUpdateBook_entityNotFoundException() throws Exception {
        BookDTO bookDTO = getBookDTOTestObj();
        bookDTO.setId("123456");
        Mockito.when(bookService.updateBook(any(BookDTO.class))).thenThrow(new EntityNotFoundException("Book with id " + bookDTO.getId() + " not found"));

        mockMvc.perform(put("/books/{id}", bookDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with id " + bookDTO.getId() + " not found"))
                .andExpect(jsonPath("$.errorCode").value("ENTITY_NOT_FOUND"));
    }


    private BookDTO getBookDTOTestObj() {
        BookDTO book = new BookDTO();

        book.setTitle("Test Book");
        book.setAuthor("Author A");
        book.setIsbn("1234567890123");
        book.setPublishedDate(LocalDate.of(2024, 1, 1));
        return book;
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public BookServiceInterface bookService() {
            return Mockito.mock(BookServiceInterface.class);
        }
    }
}
