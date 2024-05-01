package com.zaidi.aeonbank.book;

import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;

    private final Book book = new Book("9781492072508", "Think Java", "Allen B. Downey and Chris Mayfield");

    /**
     * Register book
     * Result : book saved successfully
     */
    @Test
    void register() {
        bookService.register(book);
        Mockito
                .verify(bookRepository)
                .save(book);
    }

    /**
     * Register book with same isbn and title and author
     * Result : book saved successfully
     */
    @Test
    void registerWithSameIsbnAndTitleAndAuthor() {
        Mockito
                .when(bookRepository.findFirstByIsbn(book.getIsbn()))
                .thenReturn(Optional.of(book));
        bookService.register(book);
        Mockito
                .verify(bookRepository)
                .save(book);
    }

    /**
     * Register book with same isbn but different title and author
     * Result : exception thrown
     */
    @Test
    void registerWithSameIsbnButDifferentTitleAndAuthor() {
        Mockito
                .when(bookRepository.findFirstByIsbn(book.getIsbn()))
                .thenReturn(Optional.of(book));
        Book toRegisterBook = new Book(book.getIsbn(), "Scala Cookbook", "Alvin Alexander");
        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () -> bookService.register(toRegisterBook));
        Assertions.assertEquals("The title or author does not match with existing record based on the isbn", ex.getMessage());
    }

    /**
     * List of all book
     */
    @Test
    void listOfAllBook() {
        Pageable page = Pageable.unpaged();
        bookService.listOfAllBooks(page);
        Mockito
                .verify(bookRepository)
                .findAll(page);
    }

    /**
     * Borrow book
     */
    @Test
    void borrow() {
        Book existingBook = new Book(book.getIsbn(), book.getTitle(), book.getAuthor());
        Mockito
                .when(bookRepository.findById(1L))
                .thenReturn(Optional.of(existingBook));

        bookService.borrow(1L);

        ArgumentCaptor<Book> borrowedBook = ArgumentCaptor.forClass(Book.class);
        Mockito
                .verify(bookRepository)
                .save(borrowedBook.capture());
        Assertions.assertEquals(
                BookStatus.BORROWED,
                borrowedBook
                        .getValue()
                        .getBookStatus());
    }

    /**
     * Borrow book that not exists
     * expected exception
     */
    @Test
    void borrowButBookNotExist() {
        Mockito
                .when(bookRepository.findById(1L))
                .thenReturn(Optional.empty());

        EntityNotFoundException ex = Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.borrow(1L));
        Assertions.assertEquals("Cannot find book with id 1", ex.getMessage());
    }

    /**
     * Borrow book but book not available
     * expected exception
     */
    @Test
    void borrowButBookNotAvailable() {
        Book existingBook = new Book(book.getIsbn(), book.getTitle(), book.getAuthor());
        existingBook.setBookStatus(BookStatus.BORROWED);
        Mockito
                .when(bookRepository.findById(1L))
                .thenReturn(Optional.of(existingBook));

        IllegalStateException ex = Assertions.assertThrows(IllegalStateException.class, () -> bookService.borrow(1L));
        Assertions.assertEquals("Book is not available to borrow", ex.getMessage());
    }

    @Test
    void returnBook() {
        Book existingBook = new Book(book.getIsbn(), book.getTitle(), book.getAuthor());
        existingBook.setBookStatus(BookStatus.BORROWED);
        Mockito
                .when(bookRepository.findById(1L))
                .thenReturn(Optional.of(existingBook));

        bookService.returnBook(1L);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        Mockito
                .verify(bookRepository)
                .save(bookArgumentCaptor.capture());
        Assertions.assertEquals(
                BookStatus.AVAILABLE,
                bookArgumentCaptor
                        .getValue()
                        .getBookStatus());
    }

    /**
     * Return book but book is not borrowed
     * We allow that
     */
    @Test
    void returnBookButBookIsNotBorrowed() {
        Book existingBook = new Book(book.getIsbn(), book.getTitle(), book.getAuthor());
        Mockito
                .when(bookRepository.findById(1L))
                .thenReturn(Optional.of(existingBook));

        bookService.returnBook(1L);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        Mockito
                .verify(bookRepository)
                .save(bookArgumentCaptor.capture());
        Assertions.assertEquals(
                BookStatus.AVAILABLE,
                bookArgumentCaptor
                        .getValue()
                        .getBookStatus());
    }

    /**
     * Return book that is not exist to the system
     * Expected exception
     */
    @Test
    void returnBookThatIsNotExist() {
        Mockito
                .when(bookRepository.findById(1L))
                .thenReturn(Optional.empty());

        EntityNotFoundException ex = Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.returnBook(1L));
        Assertions.assertEquals("Cannot find book with id 1", ex.getMessage());
    }

    @Test
    void findById() {
        Mockito
                .when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));
        Book result = bookService.findById(1L);
        Assertions.assertEquals(book, result);
    }

    /**
     * Find by id but book not found
     * expected exception
     */
    @Test
    void findByIdButNotFound() {
        Mockito
                .when(bookRepository.findById(1L))
                .thenReturn(Optional.empty());

        EntityNotFoundException ex = Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.findById(1L));
        Assertions.assertEquals("Cannot find book with id 1", ex.getMessage());
    }

}
