package com.zaidi.aeonbank.borrower;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    boolean existsByNameAndEmail(String name, String email);

}
