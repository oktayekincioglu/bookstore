package com.drawandcode.bookstore.controller;

import com.drawandcode.bookstore.dto.BookDto;
import com.drawandcode.bookstore.dto.BookListResponseDto;
import com.drawandcode.bookstore.exception.ResourceNotFoundException;
import com.drawandcode.bookstore.service.BookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
public class BookstoreControllerImpl implements BookstoreController {
    Logger logger = LoggerFactory.getLogger(BookstoreControllerImpl.class);

    public static final String BOOK_GET_URL = "/api/v1/bookstore/books/";

    @Autowired
    private BookService bookService;

    public ResponseEntity<Void> addNewBook(@Valid BookDto book) {
        logger.debug("AddNewBook request received. Content: {}", book.toString());
        Long id = bookService.addNewBook(book);
        URI bookUri = URI.create(BOOK_GET_URL + id);
        return ResponseEntity.created(bookUri).build();
    }

    public ResponseEntity<Void> updateBook(@PathVariable Long id, @RequestBody @Valid BookDto book) {
        logger.debug("UpdateBook request received. Id: {}, content: {}", id, book.toString());
        bookService.updateBook(id, book);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        logger.debug("DeleteBook request received. Id: {}", id);
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<BookListResponseDto> getAllBooks() {
        logger.debug("GetAllBooks request received.");
        BookListResponseDto listResponseDto = new BookListResponseDto(bookService.getAllBooks());
        return ResponseEntity.ok(listResponseDto);
    }

    public ResponseEntity<BookDto> getBookById(Long id) {
        logger.debug("GetBookById request received. Id: {}", id);
        Optional<BookDto> book = bookService.getBookById(id);
        if (book.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Book with the specified id (%d) does not exist.", id));
        }

        return ResponseEntity.ok(book.get());
    }

    public ResponseEntity<BookListResponseDto> searchBooks(@RequestParam(required = false) String isbn, @RequestParam(required = false) String title) {
        logger.debug("SearchBooks request received. Isbn: {} Title: {}", isbn, title);
        BookListResponseDto listResponseDto = new BookListResponseDto(bookService.searchBooks(isbn, title));
        return ResponseEntity.ok(listResponseDto);
    }

}
