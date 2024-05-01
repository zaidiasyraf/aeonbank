package com.zaidi.aeonbank.bookborrower;

import com.zaidi.aeonbank.book.Book;
import com.zaidi.aeonbank.book.BookService;
import com.zaidi.aeonbank.borrower.Borrower;
import com.zaidi.aeonbank.borrower.BorrowerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookBorrowerServiceTest {

    @InjectMocks
    private BookBorrowerService bookBorrowerService;
    @Mock
    private BookService bookService;
    @Mock
    private BorrowerService borrowerService;
    @Mock
    private BookBorrowerRepository bookBorrowerRepository;

    private final Book book = new Book("9781492072508", "Think Java", "Allen B. Downey and Chris Mayfield");
    private final Borrower borrower = new Borrower("Dylan", "dylan@gmail.com");

    @BeforeEach
    void before() {
        book.setId(1L);
        Mockito
                .when(bookService.findById(1L))
                .thenReturn(book);
    }

    @Test
    void borrow() {
        Mockito
                .when(borrowerService.findById(1L))
                .thenReturn(borrower);

        bookBorrowerService.borrow(new BorrowBookRequest(1L, 1L));

        Mockito
                .verify(bookService)
                .borrow(1L);
        ArgumentCaptor<BookBorrower> captor = ArgumentCaptor.forClass(BookBorrower.class);
        Mockito
                .verify(bookBorrowerRepository)
                .save(captor.capture());
        BookBorrower expected = new BookBorrower(book, borrower, BookBorrowerAction.BORROW);
        Assertions.assertEquals(expected.getBook(),
                captor
                        .getValue()
                        .getBook());
        Assertions.assertEquals(expected.getBorrower(),
                captor
                        .getValue()
                        .getBorrower());
        Assertions.assertEquals(expected.getAction(),
                captor
                        .getValue()
                        .getAction());
    }

    @Test
    void returnBook() {
        bookBorrowerService.returnBook(1L);

        Mockito
                .verify(bookService)
                .returnBook(1L);
        ArgumentCaptor<BookBorrower> captor = ArgumentCaptor.forClass(BookBorrower.class);
        Mockito
                .verify(bookBorrowerRepository)
                .save(captor.capture());
        BookBorrower expected = new BookBorrower(book, BookBorrowerAction.RETURN);
        Assertions.assertEquals(expected.getBook(),
                captor
                        .getValue()
                        .getBook());
        Assertions.assertEquals(expected.getBorrower(),
                captor
                        .getValue()
                        .getBorrower());
        Assertions.assertEquals(expected.getAction(),
                captor
                        .getValue()
                        .getAction());
    }

}
