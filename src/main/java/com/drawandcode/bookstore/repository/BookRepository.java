package com.drawandcode.bookstore.repository;

import com.drawandcode.bookstore.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findByIsbn(String isbn);

    List<Book> findByTitleContainsIgnoreCase(String title);
}
