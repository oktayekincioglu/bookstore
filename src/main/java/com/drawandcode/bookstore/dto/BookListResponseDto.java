package com.drawandcode.bookstore.dto;

import java.util.List;

public class BookListResponseDto {
    private List<BookDto> books;

    public BookListResponseDto() {
    }

    public BookListResponseDto(List<BookDto> books) {
        this.books = books;
    }

    public List<BookDto> getBooks() {
        return books;
    }

    public void setBooks(List<BookDto> books) {
        this.books = books;
    }
}
