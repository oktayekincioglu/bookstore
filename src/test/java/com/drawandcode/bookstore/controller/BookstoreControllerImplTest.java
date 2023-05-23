package com.drawandcode.bookstore.controller;

import com.drawandcode.bookstore.dto.BookDto;
import com.drawandcode.bookstore.exception.ResourceNotFoundException;
import com.drawandcode.bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_BOOK_DTO;
import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_ISBN;
import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_PUBLISHER;
import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_TITLE;
import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_YEAR;
import static com.drawandcode.bookstore.TestConstants.MICROSERVICES_DTO;
import static com.drawandcode.bookstore.TestConstants.MICROSERVICES_ISBN;
import static com.drawandcode.bookstore.TestConstants.MICROSERVICES_PUBLISHER;
import static com.drawandcode.bookstore.TestConstants.MICROSERVICES_TITLE;
import static com.drawandcode.bookstore.TestConstants.MICROSERVICES_YEAR;
import static com.drawandcode.bookstore.TestHelper.verifyBook;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookstoreControllerImplTest {
    @MockBean
    private BookService bookService;

    @Captor
    private ArgumentCaptor<BookDto> bookDtoCaptor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("POST /api/v1/bookstore/books success")
    void testAddBookSuccess() throws Exception {
        BookDto book = DESIGN_PATTERNS_BOOK_DTO;

        when(bookService.addNewBook(any())).thenReturn(1L);
        mockMvc.perform(
                // Build Request
                post("/api/v1/bookstore/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(book)))

                // Validate response
                .andExpect(status().isCreated());
    }


    @Test
    @DisplayName("POST /api/v1/bookstore/books validation errors")
    void testAddBookValidationErrors() throws Exception {
        String titleLessThan3Chars = "Ab";

        BookDto book = new BookDto(titleLessThan3Chars,
                DESIGN_PATTERNS_ISBN,
                DESIGN_PATTERNS_YEAR,
                null,
                Collections.emptyList()
        );

        mockMvc.perform(
                        // Build Request
                        post("/api/v1/bookstore/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(book)))

                // Validate response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation error. Check 'errors' field for details.")))
                .andExpect(jsonPath("$.validationErrors.length()", is(3)))
                .andExpect(jsonPath("$.validationErrors", hasItems(
                        "At least one 'author' is required",
                        "The field 'title' must contain at least 3 characters",
                        "The field 'publisher' is required"
                )));
    }

    @Test
    @DisplayName("POST /api/v1/bookstore/books 'bad request' error")
    void testAddBookBadRequestError() throws Exception {
        mockMvc.perform(
                        // Build Request
                        post("/api/v1/bookstore/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("non-json body"))

                // Validate response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", startsWith("JSON parse error:")));
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("PUT /api/v1/bookstore/books/{id} success")
    void updateBook() throws Exception {
        BookDto book = DESIGN_PATTERNS_BOOK_DTO;

        mockMvc.perform(
                        // Build Request
                        put("/api/v1/bookstore/books/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(book)))

                // Validate response
                .andExpect(status().isOk());

        verify(bookService, times(1)).updateBook(eq(1L), bookDtoCaptor.capture());
        verifyBook(bookDtoCaptor.getValue(), book);
    }

    @Test
    @DisplayName("PUT /api/v1/bookstore/books/{id} not-found")
    void updateBookNotFound() throws Exception {
        BookDto book = DESIGN_PATTERNS_BOOK_DTO;

        String errorMessage = "Book with the specified id (1) does not exist.";
        doThrow(new ResourceNotFoundException(errorMessage)).when(bookService).updateBook(eq(1L), any());

        mockMvc.perform(
                        // Build Request
                        put("/api/v1/bookstore/books/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(book)))

                // Validate response
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(errorMessage)));

    }

    @Test
    @DisplayName("DELETE /api/v1/bookstore/books/{id}")
    void deleteBook() throws Exception {
        mockMvc.perform(
                        // Build Request
                        delete("/api/v1/bookstore/books/1")
                                .contentType(MediaType.APPLICATION_JSON))

                // Validate response
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/v1/bookstore/books/{id} not-found")
    void deleteBookNotFound() throws Exception {
        String errorMessage = "Book with the specified id (1) does not exist.";
        doThrow(new ResourceNotFoundException(errorMessage)).when(bookService).deleteBook(eq(1L));

        mockMvc.perform(
                        // Build Request
                        delete("/api/v1/bookstore/books/1")
                                .contentType(MediaType.APPLICATION_JSON))

                // Validate response
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

    @Test
    @DisplayName("GET /api/v1/bookstore/books")
    void getAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(DESIGN_PATTERNS_BOOK_DTO, MICROSERVICES_DTO));
        ResultActions resultActions = mockMvc.perform(
                        // Build Request
                        get("/api/v1/bookstore/books")
                                .contentType(MediaType.APPLICATION_JSON))

                // Validate response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books.length()", is(2)))
                .andExpect(jsonPath("$.books[0].title", is(DESIGN_PATTERNS_TITLE)))
                .andExpect(jsonPath("$.books[0].isbn", is(DESIGN_PATTERNS_ISBN)))
                .andExpect(jsonPath("$.books[0].publisher", is(DESIGN_PATTERNS_PUBLISHER)))
                .andExpect(jsonPath("$.books[0].publicationYear", is(DESIGN_PATTERNS_YEAR)))
                .andExpect(jsonPath("$.books[0].authors.length()", is(3)))

                .andExpect(jsonPath("$.books[1].title", is(MICROSERVICES_TITLE)))
                .andExpect(jsonPath("$.books[1].isbn", is(MICROSERVICES_ISBN)))
                .andExpect(jsonPath("$.books[1].publisher", is(MICROSERVICES_PUBLISHER)))
                .andExpect(jsonPath("$.books[1].publicationYear", is(MICROSERVICES_YEAR)))
                .andExpect(jsonPath("$.books[1].authors.length()", is(1)));
    }

    @Test
    @DisplayName("GET /api/v1/bookstore/books/{id}")
    void getBookById() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(Optional.ofNullable(DESIGN_PATTERNS_BOOK_DTO));
        mockMvc.perform(
                        // Build Request
                        get("/api/v1/bookstore/books/1")
                                .contentType(MediaType.APPLICATION_JSON))

                // Validate response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(DESIGN_PATTERNS_TITLE)))
                .andExpect(jsonPath("$.isbn", is(DESIGN_PATTERNS_ISBN)))
                .andExpect(jsonPath("$.publisher", is(DESIGN_PATTERNS_PUBLISHER)))
                .andExpect(jsonPath("$.publicationYear", is(DESIGN_PATTERNS_YEAR)))
                .andExpect(jsonPath("$.authors.length()", is(3)));
    }

    @Test
    @DisplayName("GET /api/v1/bookstore/books/{id} not-found")
    void getBookByIdNotFound() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(
                        // Build Request
                        get("/api/v1/bookstore/books/1")
                                .contentType(MediaType.APPLICATION_JSON))

                // Validate response
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Book with the specified id (1) does not exist.")));
    }

    @Test
    @DisplayName("GET /api/v1/bookstore/books/search?isbn=*")
    void searchBooksIsbn() throws Exception {
        when(bookService.searchBooks(MICROSERVICES_ISBN, null)).thenReturn(List.of(MICROSERVICES_DTO));

        mockMvc.perform(
                        // Build Request
                        get("/api/v1/bookstore/books/search?isbn=" + MICROSERVICES_ISBN)
                                .contentType(MediaType.APPLICATION_JSON))

                // Validate response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books.length()", is(1)))
                .andExpect(jsonPath("$.books[0].title", is(MICROSERVICES_TITLE)))
                .andExpect(jsonPath("$.books[0].isbn", is(MICROSERVICES_ISBN)))
                .andExpect(jsonPath("$.books[0].publisher", is(MICROSERVICES_PUBLISHER)))
                .andExpect(jsonPath("$.books[0].publicationYear", is(MICROSERVICES_YEAR)))
                .andExpect(jsonPath("$.books[0].authors.length()", is(1)));
    }

    @Test
    @DisplayName("GET /api/v1/bookstore/books/search?title=design")
    void searchBooksTitle() throws Exception {
        when(bookService.searchBooks(null, "design")).thenReturn(List.of(DESIGN_PATTERNS_BOOK_DTO));

        mockMvc.perform(
                        // Build Request
                        get("/api/v1/bookstore/books/search?title=design")
                                .contentType(MediaType.APPLICATION_JSON))

                // Validate response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books.length()", is(1)))
                .andExpect(jsonPath("$.books[0].title", is(DESIGN_PATTERNS_TITLE)))
                .andExpect(jsonPath("$.books[0].isbn", is(DESIGN_PATTERNS_ISBN)))
                .andExpect(jsonPath("$.books[0].publisher", is(DESIGN_PATTERNS_PUBLISHER)))
                .andExpect(jsonPath("$.books[0].publicationYear", is(DESIGN_PATTERNS_YEAR)))
                .andExpect(jsonPath("$.books[0].authors.length()", is(3)));
    }

    @Test
    @DisplayName("GET /api/v1/bookstore/books/search bad-request")
    void searchBooksBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Invalid search request. Either 'isbn' or 'title' must be provided."))
                .when(bookService).searchBooks(any(), any());

        mockMvc.perform(
                        // Build Request
                        get("/api/v1/bookstore/books/search?title=design")
                                .contentType(MediaType.APPLICATION_JSON))

                // Validate response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        is("Invalid argument: Invalid search request. Either 'isbn' or 'title' must be provided.")));

    }
}