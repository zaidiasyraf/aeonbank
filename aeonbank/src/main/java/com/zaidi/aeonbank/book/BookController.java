package com.zaidi.aeonbank.book;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class BookController {

    private final BookService bookService;

    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Register a book", description = "If there is existing record with same isbn as request, the author and title should be match!. The response is Book object")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                                                                   schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "Constraint breached",
                         content = @Content)})
    @PostMapping(value = "/book/register")
    // Usually we will return with different object like response dto instead of plain entity object
    public Book register(@RequestBody BookRegister bookRegister) {
        return bookService.register(bookRegister.asBook());
    }

    @Operation(summary = "Get all book", description = "Return all book based on pagination, Add pageSize and pageNumber to control pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                                                                   schema = @Schema(implementation = Book.class))}),})
    @GetMapping(value = "/books")
    // Usually we will return with different object like response dto instead of plain entity object
    public Page<Book> getAllBooks(@RequestParam(defaultValue = "10", required = false) int pageSize, @RequestParam(defaultValue = "0", required = false) int pageNumber) {
        return bookService.listOfAllBooks(PageRequest.of(pageNumber, pageSize));
    }

}
