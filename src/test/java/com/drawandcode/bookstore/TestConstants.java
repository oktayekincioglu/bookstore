package com.drawandcode.bookstore;

import com.drawandcode.bookstore.dto.AuthorDto;
import com.drawandcode.bookstore.dto.BookDto;
import com.drawandcode.bookstore.entity.Author;
import com.drawandcode.bookstore.entity.Book;
import com.drawandcode.bookstore.entity.Publisher;

import java.util.List;
import java.util.Set;

public class TestConstants {
    public static final String DESIGN_PATTERNS_TITLE = "Design patterns : Elements of Reusable object-oriented software";
    public static final String DESIGN_PATTERNS_ISBN = "0201633612";
    public static final Integer DESIGN_PATTERNS_YEAR = 1995;
    public static final String DESIGN_PATTERNS_PUBLISHER = "Addison-Wesley";
    public static final Author DESIGN_PATTERNS_AUTHORS_0 = new Author("Erich", "Gamma");
    public static final Author DESIGN_PATTERNS_AUTHORS_1 = new Author("Richard", "Helm");
    public static final Author DESIGN_PATTERNS_AUTHORS_2 = new Author("Ralph", "Johnson");

    public static final Set<Author> DESIGN_PATTERNS_AUTHORS = Set.of(
            DESIGN_PATTERNS_AUTHORS_0,
            DESIGN_PATTERNS_AUTHORS_1,
            DESIGN_PATTERNS_AUTHORS_2);

    public static final List<AuthorDto> DESIGN_PATTERNS_AUTHORS_DTO = List.of(
            new AuthorDto("Erich", "Gamma"),
            new AuthorDto("Richard", "Helm"),
            new AuthorDto("Ralph", "Johnson"));

    public static final String MICROSERVICES_TITLE = "Building Microservices Second Edition: Designing Fine-Grained Systems";
    public static final String MICROSERVICES_ISBN = "1492034029";
    public static final Integer MICROSERVICES_YEAR = 2021;
    public static final String MICROSERVICES_PUBLISHER = "O'Reilly";
    public static final Set<Author> MICROSERVICES_AUTHORS = Set.of(new Author("Sam", "Newman"));
    public static final List<AuthorDto> MICROSERVICES_AUTHORS_DTO = List.of(new AuthorDto("Sam", "Newman"));


    public static final String DDD_TITLE = "Domain-Driven Design: Tackling Complexity in the Heart of Software";
    public static final String DDD_ISBN = "0321125215";
    public static final Integer DDD_YEAR = 2003;
    public static final String DDD_PUBLISHER = "Addison-Wesley";
    public static final Set<Author> DDD_AUTHORS = Set.of(new Author("Eric",  "Evans"));


    public static final BookDto DESIGN_PATTERNS_BOOK_DTO = new BookDto(
            DESIGN_PATTERNS_TITLE,
            DESIGN_PATTERNS_ISBN,
            DESIGN_PATTERNS_YEAR,
            DESIGN_PATTERNS_PUBLISHER,
            DESIGN_PATTERNS_AUTHORS_DTO
            );

    public static final BookDto MICROSERVICES_DTO = new BookDto(
            MICROSERVICES_TITLE,
            MICROSERVICES_ISBN,
            MICROSERVICES_YEAR,
            MICROSERVICES_PUBLISHER,
            MICROSERVICES_AUTHORS_DTO
    );


    public static final Book DESIGN_PATTERNS_BOOK = new Book(
            DESIGN_PATTERNS_TITLE,
            DESIGN_PATTERNS_ISBN,
            DESIGN_PATTERNS_YEAR,
            new Publisher(DESIGN_PATTERNS_PUBLISHER),
            DESIGN_PATTERNS_AUTHORS
    );

    public static final Book MICROSERVICES_BOOK = new Book(
            MICROSERVICES_TITLE,
            MICROSERVICES_ISBN,
            MICROSERVICES_YEAR,
            new Publisher(MICROSERVICES_PUBLISHER),
            MICROSERVICES_AUTHORS
    );

    public static final Book DDD_BOOK = new Book(
            DDD_TITLE,
            DDD_ISBN,
            DDD_YEAR,
            new Publisher(DDD_PUBLISHER),
            DDD_AUTHORS
    );

}
