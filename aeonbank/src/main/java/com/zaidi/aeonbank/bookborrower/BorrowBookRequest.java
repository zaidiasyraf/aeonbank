package com.zaidi.aeonbank.bookborrower;

import jakarta.validation.constraints.NotNull;

public record BorrowBookRequest(@NotNull(message = "Book id cannot be null") Long bookId, @NotNull(message = "Borrower id cannot be null") Long borrowerId) {

}
