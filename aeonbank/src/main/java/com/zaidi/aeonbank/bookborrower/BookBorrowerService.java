package com.zaidi.aeonbank.bookborrower;

import com.zaidi.aeonbank.book.Book;
import com.zaidi.aeonbank.book.BookService;
import com.zaidi.aeonbank.borrower.Borrower;
import com.zaidi.aeonbank.borrower.BorrowerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookBorrowerService {

    private final BookService bookService;
    private final BorrowerService borrowerService;
    private final BookBorrowerRepository bookBorrowerRepository;

    public BookBorrowerService(final BookService bookService, final BorrowerService borrowerService, final BookBorrowerRepository bookBorrowerRepository) {
        this.bookService = bookService;
        this.borrowerService = borrowerService;
        this.bookBorrowerRepository = bookBorrowerRepository;
    }

    public BookBorrower borrow(BorrowBookRequest borrowBookRequest) {
        Book book = bookService.findById(borrowBookRequest.bookId());
        Borrower borrower = borrowerService.findById(borrowBookRequest.borrowerId());

        bookService.borrow(book.getId());
        return bookBorrowerRepository.save(new BookBorrower(book, borrower, BookBorrowerAction.BORROW));
    }

    public BookBorrower returnBook(Long bookId) {
        Book book = bookService.findById(bookId);

        bookService.returnBook(book.getId());
        return bookBorrowerRepository.save(new BookBorrower(book, BookBorrowerAction.RETURN));
    }

}
