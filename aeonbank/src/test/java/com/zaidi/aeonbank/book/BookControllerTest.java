package com.zaidi.aeonbank.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:test.properties")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void getAllBooks() throws Exception {
        bookRepository.save(new Book("9781492072508", "Think Java", "Allen B. Downey and Chris Mayfield"));
        bookRepository.save(new Book("9781492051541", "Scala Cookbook", "Alvin Alexander"));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/books"))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is2xxSuccessful())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.empty")
                        .value(false));
    }

    @Test
    void register() throws Exception {
        BookRegister bookRegister = new BookRegister("9781492072508", "Think Java", "Allen B. Downey and Chris Mayfield");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/book/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookRegister)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is2xxSuccessful())
                .andReturn();

        Book result = objectMapper.readValue(mvcResult
                .getResponse()
                .getContentAsString(), Book.class);

        Assertions.assertTrue(bookRepository
                .findById(result.getId())
                .isPresent());
    }

    /**
     * Register book with same title and author but different isbn
     * Expect the book to be saved as different book
     */
    @Test
    void registerSameTitleAndAuthorButDifferentIsbn() throws Exception {
        Book book = new Book("9781492072508", "Think Java", "Allen B. Downey and Chris Mayfield");
        book = bookRepository.save(book);

        BookRegister bookRegister = new BookRegister("111222333445512", book.getTitle(), book.getAuthor());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/book/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookRegister)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is2xxSuccessful())
                .andReturn();

        Book result = objectMapper.readValue(mvcResult
                .getResponse()
                .getContentAsString(), Book.class);

        Assertions.assertTrue(bookRepository
                .findById(result.getId())
                .isPresent());

        Assertions.assertNotEquals(book.getId(), result.getId());
    }

    /**
     * Register book with same title and author and isbn
     * Expect the book to be saved as different book
     */
    @Test
    void registerSameTitleAndAuthorAndIsbn() throws Exception {
        Book book = new Book("9781492072508", "Think Java", "Allen B. Downey and Chris Mayfield");
        book = bookRepository.save(book);

        BookRegister bookRegister = new BookRegister(book.getIsbn(), book.getTitle(), book.getAuthor());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/book/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookRegister)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is2xxSuccessful())
                .andReturn();

        Book result = objectMapper.readValue(mvcResult
                .getResponse()
                .getContentAsString(), Book.class);

        Assertions.assertTrue(bookRepository
                .findById(result.getId())
                .isPresent());

        Assertions.assertNotEquals(book.getId(), result.getId());
    }

    /**
     * Register same isbn but different author and title
     * Expect 4XX exception
     * @throws Exception
     */
    @Test
    void registerSameIsbnButDifferentAuthorAndTitle() throws Exception {
        Book book = new Book("9781492072508", "Think Java", "Allen B. Downey and Chris Mayfield");
        book = bookRepository.save(book);

        BookRegister bookRegister = new BookRegister(book.getIsbn(), "Scala Cookbook", "Alvin Alexander");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/book/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookRegister)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Register same isbn but different author
     * Expect 4XX exception
     * @throws Exception
     */
    @Test
    void registerSameIsbnButDifferentAuthor() throws Exception {
        Book book = new Book("9781492072508", "Think Java", "Allen B. Downey and Chris Mayfield");
        book = bookRepository.save(book);

        BookRegister bookRegister = new BookRegister(book.getIsbn(), book.getTitle(), "Alvin Alexander");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/book/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookRegister)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Register same isbn but different title
     * Expect 4XX exception
     * @throws Exception
     */
    @Test
    void registerSameIsbnButDifferentTitle() throws Exception {
        Book book = new Book("9781492072508", "Think Java", "Allen B. Downey and Chris Mayfield");
        book = bookRepository.save(book);

        BookRegister bookRegister = new BookRegister(book.getIsbn(), "Scala Cookbook", book.getAuthor());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/book/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(bookRegister)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(HttpStatus.BAD_REQUEST.value()));
    }

}
