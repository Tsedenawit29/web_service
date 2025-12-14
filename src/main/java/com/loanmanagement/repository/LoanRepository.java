package com.loanmanagement.repository;

import com.loanmanagement.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(collectionResourceRel = "loans", path = "loans")
public interface LoanRepository extends JpaRepository<Loan, Long> {
    
    List<Loan> findByBorrowerNameContainingIgnoreCase(String borrowerName);
    
    List<Loan> findByStatus(String status);
    
    List<Loan> findByLoanType(String loanType);
    
    List<Loan> findByStatusAndLoanType(String status, String loanType);
}

