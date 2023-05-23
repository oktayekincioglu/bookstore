package com.drawandcode.bookstore;

import com.drawandcode.bookstore.dto.BookDto;
import com.drawandcode.bookstore.entity.Book;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestHelper {
    public static void verifyBook(Book actual, Book expected) {
        assertThat(actual.getTitle(), is(expected.getTitle()));
        assertThat(actual.getIsbn(), is(expected.getIsbn()));
        assertThat(actual.getPublicationYear(), is(expected.getPublicationYear()));
        assertThat(actual.getPublisher().getName(), is(expected.getPublisher().getName()));
        assertThat(actual.getAuthors().size(), is(expected.getAuthors().size()));
    }

    public static void verifyBook(BookDto actual, Book expected) {
        assertThat(actual.getTitle(), is(expected.getTitle()));
        assertThat(actual.getIsbn(), is(expected.getIsbn()));
        assertThat(actual.getPublicationYear(), is(expected.getPublicationYear()));
        assertThat(actual.getPublisher(), is(expected.getPublisher().getName()));
        assertThat(actual.getAuthors().size(), is(expected.getAuthors().size()));
    }

    public static void verifyBook(BookDto actual, BookDto expected) {
        assertThat(actual.getTitle(), is(expected.getTitle()));
        assertThat(actual.getIsbn(), is(expected.getIsbn()));
        assertThat(actual.getPublicationYear(), is(expected.getPublicationYear()));
        assertThat(actual.getPublisher(), is(expected.getPublisher()));
        assertThat(actual.getAuthors().size(), is(expected.getAuthors().size()));
    }

}
