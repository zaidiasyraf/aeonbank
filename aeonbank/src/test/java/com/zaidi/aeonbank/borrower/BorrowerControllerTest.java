package com.zaidi.aeonbank.borrower;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
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
class BorrowerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Test
    void register() throws Exception {
        String name = RandomStringUtils.randomAlphabetic(5);
        RegisterBorrower registerBorrower = new RegisterBorrower(name,  name + "ray@gmail.com");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/borrower/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(registerBorrower)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is2xxSuccessful())
                .andReturn();

        Borrower result = objectMapper.readValue(mvcResult
                .getResponse()
                .getContentAsString(), Borrower.class);

        Assertions.assertTrue(borrowerRepository
                .findById(result.getId())
                .isPresent());

        // Cannot register with same name and email
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/borrower/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(registerBorrower)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is4xxClientError());
    }

    @Test
    void registerWithBlankNameAndEmail() throws Exception {
        RegisterBorrower registerBorrower = new RegisterBorrower("", "");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/borrower/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(registerBorrower)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is4xxClientError());
    }

}
