package com.zaidi.aeonbank.borrower;

import com.zaidi.aeonbank.helper.ValidationHelper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    public BorrowerService(final BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    public Borrower register(Borrower borrower) {
        if (!ValidationHelper.isEmail(borrower.getEmail())) {
            throw new IllegalArgumentException("Email is not in the correct format");
        }
        if (borrowerRepository.existsByNameAndEmail(borrower.getName(), borrower.getEmail())) {
            throw new EntityExistsException("There is already record for given name and email");
        }
        return borrowerRepository.save(borrower);
    }

    public Borrower findById(Long borrowerId) {
        return borrowerRepository
                .findById(borrowerId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cannot find borrower with id %d", borrowerId)));
    }

}
