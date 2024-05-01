package com.zaidi.aeonbank.bookborrower;

import com.zaidi.aeonbank.audit.Auditable;
import com.zaidi.aeonbank.book.Book;
import com.zaidi.aeonbank.borrower.Borrower;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class BookBorrower extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Book book;

    @ManyToOne
    private Borrower borrower;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private BookBorrowerAction action;

    public BookBorrower() {
    }

    public BookBorrower(final Book book, final BookBorrowerAction action) {
        this.book = book;
        this.action = action;
    }

    public BookBorrower(final Book book, final Borrower borrower, final BookBorrowerAction action) {
        this.book = book;
        this.borrower = borrower;
        this.action = action;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(final Book book) {
        this.book = book;
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public void setBorrower(final Borrower borrower) {
        this.borrower = borrower;
    }

    public BookBorrowerAction getAction() {
        return action;
    }

    public void setAction(final BookBorrowerAction action) {
        this.action = action;
    }

}
