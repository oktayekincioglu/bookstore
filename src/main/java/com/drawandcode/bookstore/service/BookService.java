package com.drawandcode.bookstore.service;

import com.drawandcode.bookstore.dto.AuthorDto;
import com.drawandcode.bookstore.dto.BookDto;
import com.drawandcode.bookstore.entity.Author;
import com.drawandcode.bookstore.entity.Book;
import com.drawandcode.bookstore.entity.Publisher;
import com.drawandcode.bookstore.exception.ResourceNotFoundException;
import com.drawandcode.bookstore.repository.AuthorRepository;
import com.drawandcode.bookstore.repository.BookRepository;
import com.drawandcode.bookstore.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Objects.nonNull;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    public List<BookDto> getAllBooks() {
        Iterable<Book> books = bookRepository.findAll();
        return StreamSupport.stream(books.spliterator(), false)
                .map(this::entity2Dto)
                .collect(Collectors.toList());
    }


    public List<BookDto> searchBooks(String isbn, String title) {
        if (nonNull(isbn)) {
            return bookRepository.findByIsbn(isbn).stream()
                    .map(this::entity2Dto)
                    .collect(Collectors.toList());
        } else if (nonNull(title)) {
            return bookRepository.findByTitleContainsIgnoreCase(title).stream()
                    .map(this::entity2Dto)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Invalid search request. Either 'isbn' or 'title' must be provided.");
        }
    }

    public Optional<BookDto> getBookById(Long id) {
        return bookRepository.findById(id).map(this::entity2Dto);
    }

    public Long addNewBook(BookDto bookDto) {
        Book book = new Book();
        dto2Entity(bookDto, book);
        Book persistedBook = bookRepository.save(book);
        return persistedBook.getId();
    }

    public void updateBook(Long id, BookDto bookDto) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            throw new ResourceNotFoundException("Book with the specified id " + id);
        }

        Book book = optionalBook.get();
        dto2Entity(bookDto, book);
        bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Book with the specified id (%d) does not exist.", id));
        }
        bookRepository.delete(optionalBook.get());
    }

    private void dto2Entity(BookDto bookDto, Book book) {
        Publisher publisher = publisherRepository.findByName(bookDto.getPublisher())
                .orElse(new Publisher(bookDto.getPublisher()));

        Set<Author> authors = bookDto.getAuthors().stream()
                .map(dto -> authorRepository.findByFirstNameAndLastName(dto.getFirstName(), dto.getLastName())
                        .orElse(new Author(dto.getFirstName(), dto.getLastName())))
                .collect(Collectors.toSet());

        book.setTitle(bookDto.getTitle());
        book.setIsbn(bookDto.getIsbn());
        book.setPublicationYear(bookDto.getPublicationYear());
        book.setPublisher(publisher);
        book.setAuthors(authors);
    }

    private BookDto entity2Dto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPublicationYear(book.getPublicationYear());
        bookDto.setPublisher(book.getPublisher().getName());
        bookDto.setAuthors(book.getAuthors().stream()
                .map(aut -> new AuthorDto(aut.getId(), aut.getFirstName(), aut.getLastName()))
                .collect(Collectors.toList()));

        return bookDto;
    }

}
