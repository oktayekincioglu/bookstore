package com.drawandcode.bookstore.repository;

import com.drawandcode.bookstore.entity.Author;
import com.drawandcode.bookstore.entity.Book;
import com.drawandcode.bookstore.entity.Publisher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.drawandcode.bookstore.TestConstants.DDD_BOOK;
import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_AUTHORS;
import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_BOOK;
import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_ISBN;
import static com.drawandcode.bookstore.TestConstants.MICROSERVICES_BOOK;
import static com.drawandcode.bookstore.TestHelper.verifyBook;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private PublisherRepository publisherRepository;

    private Long designPatternsId;
    private Long microservicesId;
    private Long dddId;

    @BeforeEach
    void setUp() {
        Book persistedDesignPatterns = bookRepository.save(cloneBook(DESIGN_PATTERNS_BOOK));

        designPatternsId = persistedDesignPatterns.getId();

        Book persistedMicroservices = bookRepository.save(cloneBook(MICROSERVICES_BOOK));
        microservicesId = persistedMicroservices.getId();

        Book persistedDdd = bookRepository.save(cloneBook(DDD_BOOK));
        dddId = persistedDdd.getId();
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
        publisherRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    void shouldRunSetUpProperly() {
        long count = bookRepository.count();
        assertThat(count, is(3L));
    }

    @Test
    void shouldGetBookById() {
        Optional<Book> retrievedBook = bookRepository.findById(designPatternsId);
        assertThat(retrievedBook.isPresent(), is(true));
        verifyBook(retrievedBook.get(), DESIGN_PATTERNS_BOOK);
    }

    @Test
    void shouldDeleteBookById() {

        long countBefore = bookRepository.count();
        bookRepository.deleteById(microservicesId);
        long countAfter = bookRepository.count();
        assertThat(countAfter, is(countBefore - 1));

        Optional<Book> retrievedBook = bookRepository.findById(microservicesId);
        assertThat(retrievedBook.isEmpty(), is(true));
    }

    @Test
    void shouldUpdateBook() {
        String newTitle = "DDD";
        String newIsbn = "1234567890";

        Optional<Book> book = bookRepository.findById(dddId);
        assertThat(book.isPresent(), is(true));

        Book dddBook = book.get();
        dddBook.setTitle(newTitle);
        dddBook.setIsbn(newIsbn);
        bookRepository.save(dddBook);

        Optional<Book> bookUpdated = bookRepository.findById(dddId);
        assertThat(bookUpdated.isPresent(), is(true));
        assertThat(bookUpdated.get().getTitle(), is(newTitle));
        assertThat(bookUpdated.get().getIsbn(), is(newIsbn));
    }

    @Test
    void shouldRemoveOneOfTheAuthors() {

        Optional<Book> book = bookRepository.findById(designPatternsId);
        assertThat(book.isPresent(), is(true));

        Book designPatternsBook = book.get();
        designPatternsBook.getAuthors().removeIf(author -> author.getFirstName().equals("Ralph"));
        bookRepository.save(designPatternsBook);

        Optional<Book> bookUpdated = bookRepository.findById(designPatternsId);
        assertThat(bookUpdated.isPresent(), is(true));
        assertThat(bookUpdated.get().getAuthors().size(), is(DESIGN_PATTERNS_AUTHORS.size() - 1));
    }

    @Test
    void shouldAddAnAuthorToABook() {

        Optional<Book> book = bookRepository.findById(designPatternsId);
        assertThat(book.isPresent(), is(true));

        Book designPatternsBook = book.get();
        designPatternsBook.getAuthors().add(new Author("Robert C.", "Martin"));
        bookRepository.save(designPatternsBook);

        Optional<Book> bookUpdated = bookRepository.findById(designPatternsId);
        assertThat(bookUpdated.isPresent(), is(true));
        assertThat(bookUpdated.get().getAuthors().size(), is(DESIGN_PATTERNS_AUTHORS.size() + 1));
    }

    @Test
    void shouldFindBookByIsbn() {
        List<Book> books = bookRepository.findByIsbn(DESIGN_PATTERNS_ISBN);
        assertThat(books.size(), is(1));
        verifyBook(books.get(0), DESIGN_PATTERNS_BOOK);
    }

    @Test
    void shouldfindBookByTitleContainsIgnoreCase() {
        List<Book> books = bookRepository.findByTitleContainsIgnoreCase("domain");
        assertThat(books.size(), is(1));
        verifyBook(books.get(0), DDD_BOOK);
    }

    private Book cloneBook(Book book) {
        return new Book(book.getTitle(),
                book.getIsbn(),
                book.getPublicationYear(),
                new Publisher(book.getPublisher().getName()),
                cloneAuthors(book.getAuthors()));
    }

    private Set<Author> cloneAuthors(Set<Author> authors) {
        return authors.stream()
                .map(a -> new Author(a.getFirstName(), a.getLastName()))
                .collect(Collectors.toSet());
    }


}