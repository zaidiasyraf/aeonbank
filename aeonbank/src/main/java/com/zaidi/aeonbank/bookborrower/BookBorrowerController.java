package com.zaidi.aeonbank.bookborrower;

import com.zaidi.aeonbank.book.Book;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class BookBorrowerController {

    private final BookBorrowerService bookBorrowerService;

    public BookBorrowerController(final BookBorrowerService bookBorrowerService) {
        this.bookBorrowerService = bookBorrowerService;
    }

    @Operation(summary = "Borrow a book", description = "Need book id and borrower id, Both book and borrower must exist in database, Book also must be available for the API to success")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                                                                   schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "403", description = "Book is not available to borrow",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book or borrower not found",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping(value = "/borrow-book")
    public BookBorrower borrowBook(@RequestBody BorrowBookRequest borrowBookRequest) {
        return bookBorrowerService.borrow(borrowBookRequest);
    }

    @PostMapping(value = "/return-book/{bookId}")
    @Operation(summary = "Return a book", description = "Need book id, book must exist in database, Book status can be anything")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                                                                   schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public BookBorrower returnBook(@PathVariable Long bookId) {
        return bookBorrowerService.returnBook(bookId);
    }

}
