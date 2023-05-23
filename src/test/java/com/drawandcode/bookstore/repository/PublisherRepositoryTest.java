
package com.drawandcode.bookstore.repository;

import com.drawandcode.bookstore.entity.Publisher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.drawandcode.bookstore.TestConstants.DESIGN_PATTERNS_PUBLISHER;
import static com.drawandcode.bookstore.TestConstants.MICROSERVICES_PUBLISHER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
class PublisherRepositoryTest {
    @Autowired
    private PublisherRepository publisherRepository;

    @BeforeEach
    void setUp() {
        publisherRepository.save(new Publisher(DESIGN_PATTERNS_PUBLISHER));
        publisherRepository.save(new Publisher(MICROSERVICES_PUBLISHER));
    }

    @AfterEach
    void tearDown() {
        publisherRepository.deleteAll();
    }

    @Test
    void shouldfindPublisherByName() {
        Optional<Publisher> publisher = publisherRepository.findByName(DESIGN_PATTERNS_PUBLISHER);

        assertThat(publisher.isPresent(), is(true));
        assertThat(publisher.get().getName(), is(DESIGN_PATTERNS_PUBLISHER));
    }
}