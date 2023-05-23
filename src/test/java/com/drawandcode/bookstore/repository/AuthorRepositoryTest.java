package com.drawandcode.bookstore.repository;

import com.drawandcode.bookstore.entity.Author;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;


import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_AUTHORS_0;
import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_AUTHORS_1;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@DataJpaTest
class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        authorRepository.save(DESIGN_PATTERNS_AUTHORS_0);
        authorRepository.save(DESIGN_PATTERNS_AUTHORS_1);
    }

    @AfterEach
    void tearDown() {
        authorRepository.deleteAll();
    }

    @Test
    void findByFirstNameAndLastName() {
        Optional<Author> author = authorRepository.findByFirstNameAndLastName(DESIGN_PATTERNS_AUTHORS_0.getFirstName(),
                DESIGN_PATTERNS_AUTHORS_0.getLastName());

        assertThat(author.isPresent(), is(true));
        assertThat(author.get().getFirstName(), is(DESIGN_PATTERNS_AUTHORS_0.getFirstName()));
        assertThat(author.get().getLastName(), is(DESIGN_PATTERNS_AUTHORS_0.getLastName()));
    }
}