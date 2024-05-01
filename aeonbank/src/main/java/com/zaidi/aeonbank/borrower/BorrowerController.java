package com.zaidi.aeonbank.borrower;

import com.zaidi.aeonbank.book.Book;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(final BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @Operation(summary = "Register a borrower", description = "Create new record of borrower")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                                                                   schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "403", description = "Borrower already exist",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Constraint breached",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping(value = "/borrower/register")
    // Usually we will return with different object like response dto instead of plain entity object
    public Borrower register(@RequestBody RegisterBorrower registerBorrower) {
        return borrowerService.register(registerBorrower.borrower());
    }

}
