package com.zaidi.aeonbank.book;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookService {

    private static final Logger LOG = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;

    public BookService(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book register(Book book) {
        validateOnRegister(book);
        return bookRepository.save(book);
    }

    public Page<Book> listOfAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public void borrow(Long bookId) {
        Book toBorrowBook = findById(bookId);
        // Can move this if statement into its own method, if validation get bigger
        if (!BookStatus.AVAILABLE.equals(toBorrowBook.getBookStatus())) {
            // Can change to custom exception
            throw new IllegalStateException("Book is not available to borrow");
        }

        toBorrowBook.setBookStatus(BookStatus.BORROWED);
        bookRepository.save(toBorrowBook);
    }

    public void returnBook(Long bookId) {
        Book toReturnBook = findById(bookId);
        if (!BookStatus.BORROWED.equals(toReturnBook.getBookStatus())) {
            // Just warning is enough
            LOG.warn(
                    "Book {} are not in borrowed status, status {}",
                    bookId,
                    toReturnBook
                            .getBookStatus()
                            .name());
        }

        toReturnBook.setBookStatus(BookStatus.AVAILABLE);
        bookRepository.save(toReturnBook);
    }

    public Book findById(Long bookId) {
        return bookRepository
                .findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cannot find book with id %d", bookId)));
    }

    private void validateOnRegister(Book book) {
        bookRepository
                .findFirstByIsbn(book.getIsbn())
                .ifPresent(existingBook -> {
                    if (!existingBook
                            .getTitle()
                            .equals(book.getTitle()) || !existingBook
                            .getAuthor()
                            .equals(book.getAuthor())) {
                        // Can change to custom exception
                        throw new IllegalArgumentException("The title or author does not match with existing record based on the isbn");
                    }
                });
    }

}
