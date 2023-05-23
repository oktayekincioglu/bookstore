package com.drawandcode.bookstore.controller;

import com.drawandcode.bookstore.dto.BookDto;
import com.drawandcode.bookstore.dto.BookListResponseDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@OpenAPIDefinition(info = @Info(title = "BookStore API", version = "1.0", description = "BookStore API"))
@Tag(name = "BookStore Controller")
@RestController
@RequestMapping(path = "/api/v1/bookstore")
public interface BookstoreController {
    @Operation(summary = "The endpoint for adding a new book")
    @PostMapping("/books")
    ResponseEntity<Void> addNewBook(@RequestBody @Valid BookDto book);

    @Operation(summary = "The endpoint for updating book information")
    @PutMapping("/books/{id}")
    ResponseEntity<Void> updateBook(@Parameter(description = "The book id") @PathVariable Long id, @RequestBody @Valid BookDto book);

    @Operation(summary = "The endpoint for deleting a book")
    @DeleteMapping("/books/{id}")
    ResponseEntity<Void> deleteBook(@Parameter(description = "The book id") @PathVariable Long id);

    @Operation(summary = "The endpoint for retrieving all books")
    @GetMapping("/books")
    ResponseEntity<BookListResponseDto> getAllBooks();

    @Operation(summary = "The endpoint for retrieving book information")
    @GetMapping("/books/{id}")
    ResponseEntity<BookDto> getBookById(@Parameter(description = "The book id") @PathVariable Long id);

    @Operation(summary = "The endpoint for searching books")
    @GetMapping("/books/search")
    ResponseEntity<BookListResponseDto> searchBooks(@Parameter(description = "Isbn") @RequestParam String isbn, @Parameter(description = "Words to search in Title field") @RequestParam String title);
}
