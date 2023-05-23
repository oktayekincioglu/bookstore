package com.drawandcode.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDto {
    private Long id;

    @NotEmpty
    @Size(min = 3, message = "The field 'title' must contain at least 3 characters")
    private String title;

    @NotEmpty(message = "The field 'isbn' is required")
    private String isbn;

    private Integer publicationYear;

    @NotEmpty(message = "The field 'publisher' is required")
    private String publisher;

    @Valid
    @NotNull(message = "The field 'authors' is required")
    @Size(min = 1, message = "At least one 'author' is required")
    private List<AuthorDto> authors;

    public BookDto() {
    }

    public BookDto(String title, String isbn, Integer publicationYear, String publisher, List<AuthorDto> authors) {
        this.title = title;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.publisher = publisher;
        this.authors = authors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public List<AuthorDto> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorDto> authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDto bookDto = (BookDto) o;
        return Objects.equals(id, bookDto.id) && title.equals(bookDto.title) && isbn.equals(bookDto.isbn) && Objects.equals(publicationYear, bookDto.publicationYear) && publisher.equals(bookDto.publisher) && authors.equals(bookDto.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, isbn, publicationYear, publisher, authors);
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publicationYear=" + publicationYear +
                ", publisher='" + publisher + '\'' +
                ", authors=" + authors +
                '}';
    }
}
