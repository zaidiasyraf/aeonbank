package com.zaidi.aeonbank.bookborrower;

import java.util.Optional;
import com.zaidi.aeonbank.book.Book;
import com.zaidi.aeonbank.borrower.Borrower;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource("classpath:test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookBorrowerRepositoryTest {

    @Autowired
    private BookBorrowerRepository bookBorrowerRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    private Book book = new Book("9781492072508", "Think Java", "Allen B. Downey and Chris Mayfield");
    private Borrower borrower = new Borrower("Jim", "jim@gmail.com");
    private final BookBorrower bookBorrower = new BookBorrower(book, borrower, BookBorrowerAction.BORROW);

    @BeforeEach
    void before() {
        book = testEntityManager.persist(book);
        borrower = testEntityManager.persist(borrower);
    }

    @Test
    void save() {
        BookBorrower result = bookBorrowerRepository.save(bookBorrower);
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(bookBorrower.getBook(), result.getBook());
        Assertions.assertEquals(bookBorrower.getBorrower(), result.getBorrower());
        Assertions.assertEquals(bookBorrower.getAction(), result.getAction());
    }

    @Test
    void findById() {
        Long id = testEntityManager.persistAndGetId(bookBorrower, Long.class);
        Optional<BookBorrower> result = bookBorrowerRepository.findById(id);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(id,
                result
                        .get()
                        .getId());
        Assertions.assertEquals(bookBorrower.getBook(),
                result
                        .get()
                        .getBook());
        Assertions.assertEquals(bookBorrower.getBorrower(),
                result
                        .get()
                        .getBorrower());
        Assertions.assertEquals(bookBorrower.getAction(),
                result
                        .get()
                        .getAction());
    }

}
