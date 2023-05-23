package com.drawandcode.bookstore.repository;

import com.drawandcode.bookstore.entity.Publisher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository extends CrudRepository<Publisher, Long> {
    Optional<Publisher> findByName(String name);
}
