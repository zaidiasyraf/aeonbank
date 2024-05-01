package com.zaidi.aeonbank.bookborrower;

import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaidi.aeonbank.book.Book;
import com.zaidi.aeonbank.book.BookRepository;
import com.zaidi.aeonbank.book.BookStatus;
import com.zaidi.aeonbank.borrower.Borrower;
import com.zaidi.aeonbank.borrower.BorrowerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:test.properties")
class BookBorrowerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowerRepository borrowerRepository;
    @Autowired
    private BookBorrowerRepository bookBorrowerRepository;

    @Test
    void borrowBook() throws Exception {
        Book book = bookRepository.save(new Book("9781492072508", "Think Java", "Allen B. Downey and Chris Mayfield"));
        Borrower borrower = borrowerRepository.save(new Borrower("Kim", "kim@gmail.com"));

        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(book.getId(), borrower.getId());
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/borrow-book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(borrowBookRequest)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is2xxSuccessful())
                .andReturn();

        BookBorrower result = objectMapper.readValue(mvcResult
                .getResponse()
                .getContentAsString(), BookBorrower.class);

        Assertions.assertTrue(bookBorrowerRepository.existsById(result.getId()));
        Optional<Book> resultBook = bookRepository.findById(book.getId());
        Assertions.assertTrue(resultBook.isPresent());
        Assertions.assertEquals(
                BookStatus.BORROWED,
                resultBook
                        .get()
                        .getBookStatus());

        // Cannot borrow the borrowed book more than once until the book is returned
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/borrow-book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(borrowBookRequest)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is4xxClientError());
    }

    @Test
    void returnBook() throws Exception {
        Book book = bookRepository.save(new Book("9781492072508", "Think Java", "Allen B. Downey and Chris Mayfield"));

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post(String.format("/api/v1/return-book/%d", book.getId())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is2xxSuccessful())
                .andReturn();

        BookBorrower result = objectMapper.readValue(mvcResult
                .getResponse()
                .getContentAsString(), BookBorrower.class);

        Assertions.assertTrue(bookBorrowerRepository.existsById(result.getId()));
        Optional<Book> resultBook = bookRepository.findById(book.getId());
        Assertions.assertTrue(resultBook.isPresent());
        Assertions.assertEquals(
                BookStatus.AVAILABLE,
                resultBook
                        .get()
                        .getBookStatus());
    }

}
