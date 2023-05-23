package com.drawandcode.bookstore.service;

import com.drawandcode.bookstore.dto.BookDto;
import com.drawandcode.bookstore.entity.Book;
import com.drawandcode.bookstore.entity.Publisher;
import com.drawandcode.bookstore.exception.ResourceNotFoundException;
import com.drawandcode.bookstore.repository.AuthorRepository;
import com.drawandcode.bookstore.repository.BookRepository;
import com.drawandcode.bookstore.repository.PublisherRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_AUTHORS;
import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_AUTHORS_DTO;
import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_ISBN;
import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_PUBLISHER;
import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_TITLE;
import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_YEAR;
import static com.drawandcode.bookstore.TestConstants.MICROSERVICES_AUTHORS;
import static com.drawandcode.bookstore.TestConstants.MICROSERVICES_ISBN;
import static com.drawandcode.bookstore.TestConstants.MICROSERVICES_PUBLISHER;
import static com.drawandcode.bookstore.TestConstants.MICROSERVICES_TITLE;
import static com.drawandcode.bookstore.TestConstants.MICROSERVICES_YEAR;
import static com.drawandcode.bookstore.TestHelper.verifyBook;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Captor
    private ArgumentCaptor<Book> bookCaptor;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(bookDesignPatterns(), bookMicroservices()));
        List<BookDto> books = bookService.getAllBooks();

        verify(bookRepository, times(1)).findAll();

        assertThat(books.size(), is(2));
        Optional<BookDto> book = books.stream()
                .filter(b -> b.getId().equals(1L))
                .findFirst();
        assertThat(book.isPresent(), is(true));
        assertThat(book.get().getId(), is(1L));
        verifyBook(book.get(), bookDesignPatterns());
    }

    @Test
    void shouldSearchBooksByIsbn() {
        when(bookRepository.findByIsbn(MICROSERVICES_ISBN)).thenReturn(List.of(bookMicroservices()));

        List<BookDto> books = bookService.searchBooks(MICROSERVICES_ISBN, null);

        verify(bookRepository, times(1)).findByIsbn(MICROSERVICES_ISBN);

        assertThat(books.size(), is(1));
        BookDto book = books.get(0);
        assertThat(book.getId(), is(2L));
        verifyBook(book, bookMicroservices());
    }

    @Test
    void shouldSearchBooksByTitle() {
        String searchWord = "micro";
        when(bookRepository.findByTitleContainsIgnoreCase(searchWord)).thenReturn(List.of(bookMicroservices()));

        List<BookDto> books = bookService.searchBooks(null, searchWord);
        verify(bookRepository, times(1)).findByTitleContainsIgnoreCase(searchWord);

        assertThat(books.size(), is(1));
        BookDto book = books.get(0);

        assertThat(book.getId(), is(2L));
        verifyBook(book, bookMicroservices());
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenSearchBooksWithoutParameter() {
        assertThrows(IllegalArgumentException.class, () -> bookService.searchBooks(null, null));
    }

    @Test
    void shouldGetBookById() {
        when(bookRepository.findById(2L)).thenReturn(Optional.ofNullable(bookMicroservices()));

        Optional<BookDto> book = bookService.getBookById(2L);
        verify(bookRepository, times(1)).findById(2L);

        assertThat(book.isPresent(), is(true));
        assertThat(book.get().getId(), is(2L));
        verifyBook(book.get(), bookMicroservices());
    }

    @Test
    void shouldAddNewBook() {
        BookDto bookDto = bookDesignPatternsDto();

        when(bookRepository.save(any())).thenReturn(bookDesignPatterns());
        Long result = bookService.addNewBook(bookDto);
        assertThat(result, is(1L));

        verify(bookRepository, times(1)).save(bookCaptor.capture());

        Book book = bookCaptor.getValue();
        verifyBook(book, bookDesignPatterns());
    }

    @Test
    void shouldUpdateBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(bookDesignPatterns()));
        BookDto bookDto = bookDesignPatternsDto();
        when(bookRepository.save(any())).thenReturn(bookDesignPatterns());
        bookService.updateBook(1L, bookDto);

        verify(bookRepository, times(1)).save(bookCaptor.capture());

        Book book = bookCaptor.getValue();
        assertThat(book.getId(), is(1L));
        verifyBook(book, bookDesignPatterns());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdateBookDoesNotExist() {
        BookDto bookDto = bookDesignPatternsDto();
        when(bookRepository.save(any())).thenReturn(bookDesignPatterns());
        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(1L, bookDto));
    }

    @Test
    void shouldDeleteBook() {
        Book bookToBeDeleted = bookMicroservices();
        when(bookRepository.findById(2L)).thenReturn(Optional.ofNullable(bookToBeDeleted));
        bookService.deleteBook(2L);
        verify(bookRepository, times(1)).delete(bookToBeDeleted);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeleteBookDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(2L));
    }

    private Book bookDesignPatterns() {
        return new Book(1L,
                DESIGN_PATTERNS_TITLE,
                DESIGN_PATTERNS_ISBN,
                DESIGN_PATTERNS_YEAR,
                new Publisher(DESIGN_PATTERNS_PUBLISHER),
                DESIGN_PATTERNS_AUTHORS);
    }

    private BookDto bookDesignPatternsDto() {
        return new BookDto(DESIGN_PATTERNS_TITLE,
                DESIGN_PATTERNS_ISBN,
                DESIGN_PATTERNS_YEAR,
                DESIGN_PATTERNS_PUBLISHER,
                DESIGN_PATTERNS_AUTHORS_DTO);
    }

    private Book bookMicroservices() {
        return new Book(2L,
                MICROSERVICES_TITLE,
                MICROSERVICES_ISBN,
                MICROSERVICES_YEAR,
                new Publisher(MICROSERVICES_PUBLISHER),
                MICROSERVICES_AUTHORS);
    }


}