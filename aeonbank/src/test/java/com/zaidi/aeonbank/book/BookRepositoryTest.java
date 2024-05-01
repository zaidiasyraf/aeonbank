package com.zaidi.aeonbank.book;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource("classpath:test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    private Book book = new Book("9781492072508", "Think Java", "Allen B. Downey and Chris Mayfield");

    @Test
    void save() {
        Book result = bookRepository.save(book);
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(book.getIsbn(), result.getIsbn());
        Assertions.assertEquals(book.getAuthor(), result.getAuthor());
        Assertions.assertEquals(book.getTitle(), result.getTitle());
        Assertions.assertEquals(book.getBookStatus(), result.getBookStatus());
    }

    @Test
    void findById() {
        Long id = testEntityManager.persistAndGetId(book, Long.class);
        Optional<Book> result = bookRepository.findById(id);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(
                id,
                result
                        .get()
                        .getId());
        Assertions.assertEquals(
                book.getIsbn(),
                result
                        .get()
                        .getIsbn());
        Assertions.assertEquals(
                book.getAuthor(),
                result
                        .get()
                        .getAuthor());
        Assertions.assertEquals(
                book.getTitle(),
                result
                        .get()
                        .getTitle());
        Assertions.assertEquals(
                book.getBookStatus(),
                result
                        .get()
                        .getBookStatus());
    }

    @Test
    void findFirstByIsbn() {
        bookRepository.save(book);

        Optional<Book> result = bookRepository.findFirstByIsbn(book.getIsbn());
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(
                book.getIsbn(),
                result
                        .get()
                        .getIsbn());
        Assertions.assertEquals(
                book.getAuthor(),
                result
                        .get()
                        .getAuthor());
        Assertions.assertEquals(
                book.getTitle(),
                result
                        .get()
                        .getTitle());
        Assertions.assertEquals(
                book.getBookStatus(),
                result
                        .get()
                        .getBookStatus());

        Optional<Book> resultNotFound = bookRepository.findFirstByIsbn("111222333");
        Assertions.assertFalse(resultNotFound.isPresent());
    }

}
