package com.zaidi.aeonbank.borrower;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource("classpath:test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BorrowerRepositoryTest {

    @Autowired
    private BorrowerRepository borrowerRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    private final Borrower borrower = new Borrower("Jim", "jim@gmail.com");

    @Test
    void save() {
        Borrower result = borrowerRepository.save(borrower);
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(borrower.getName(), result.getName());
        Assertions.assertEquals(borrower.getEmail(), result.getEmail());
    }

    @Test
    void findById() {
        Long id = testEntityManager.persistAndGetId(borrower, Long.class);
        Optional<Borrower> result = borrowerRepository.findById(id);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(id, result.get().getId());
        Assertions.assertEquals(borrower.getName(), result.get().getName());
        Assertions.assertEquals(borrower.getEmail(), result.get().getEmail());
    }

}
