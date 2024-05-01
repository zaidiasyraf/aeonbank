package com.zaidi.aeonbank.borrower;

import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BorrowerServiceTest {

    @InjectMocks
    private BorrowerService borrowerService;
    @Mock
    private BorrowerRepository borrowerRepository;

    private final Borrower borrower = new Borrower("Mike", "mike@gmail.com");

    @Test
    void register() {
        borrowerService.register(borrower);
        Mockito
                .verify(borrowerRepository)
                .save(borrower);

        // Register with incorrect email
        borrower.setEmail("not-an-email");
        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () -> borrowerService.register(borrower));
        Assertions.assertEquals("Email is not in the correct format", ex.getMessage());
    }

    @Test
    void findById() {
        Mockito
                .when(borrowerRepository.findById(1L))
                .thenReturn(Optional.of(borrower));
        Borrower result = borrowerService.findById(1L);
        Assertions.assertEquals(borrower, result);
    }

    @Test
    void findByIdButNotFound() {
        Mockito
                .when(borrowerRepository.findById(1L))
                .thenReturn(Optional.empty());

        EntityNotFoundException ex = Assertions.assertThrows(EntityNotFoundException.class, () -> borrowerService.findById(1L));
        Assertions.assertEquals("Cannot find borrower with id 1", ex.getMessage());
    }


}
