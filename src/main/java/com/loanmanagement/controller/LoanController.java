package com.loanmanagement.controller;

import com.loanmanagement.model.Loan;
import com.loanmanagement.repository.LoanRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * REST Controller for Loan Management
 * Implements HATEOAS (Hypermedia as the Engine of Application State)
 * by adding links to resource representations
 */
@RestController
@RequestMapping("/api/loans")
public class LoanController {
    
    private final LoanRepository loanRepository;
    
    public LoanController(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }
    
    /**
     * Get all loans with HATEOAS links
     */
    @GetMapping
    public CollectionModel<EntityModel<Loan>> getAllLoans() {
        List<EntityModel<Loan>> loans = loanRepository.findAll().stream()
                .map(loan -> EntityModel.of(loan,
                        linkTo(methodOn(LoanController.class).getLoanById(loan.getId())).withSelfRel(),
                        linkTo(methodOn(LoanController.class).getAllLoans()).withRel("loans")))
                .collect(Collectors.toList());
        
        return CollectionModel.of(loans,
                linkTo(methodOn(LoanController.class).getAllLoans()).withSelfRel());
    }
    
    /**
     * Get a single loan by ID with HATEOAS links
     */
    @GetMapping("/{id}")
    public EntityModel<Loan> getLoanById(@PathVariable Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
        
        return EntityModel.of(loan,
                linkTo(methodOn(LoanController.class).getLoanById(id)).withSelfRel(),
                linkTo(methodOn(LoanController.class).getAllLoans()).withRel("loans"),
                linkTo(methodOn(LoanController.class).updateLoan(id, loan)).withRel("update"),
                linkTo(methodOn(LoanController.class).deleteLoan(id)).withRel("delete"));
    }
    
    /**
     * Create a new loan with HATEOAS links
     */
    @PostMapping
    public ResponseEntity<EntityModel<Loan>> createLoan(@Valid @RequestBody Loan loan) {
        Loan savedLoan = loanRepository.save(loan);
        
        EntityModel<Loan> entityModel = EntityModel.of(savedLoan,
                linkTo(methodOn(LoanController.class).getLoanById(savedLoan.getId())).withSelfRel(),
                linkTo(methodOn(LoanController.class).getAllLoans()).withRel("loans"));
        
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
    
    /**
     * Update an existing loan
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Loan>> updateLoan(@PathVariable Long id, 
                                                         @Valid @RequestBody Loan loanDetails) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
        
        loan.setBorrowerName(loanDetails.getBorrowerName());
        loan.setLoanAmount(loanDetails.getLoanAmount());
        loan.setInterestRate(loanDetails.getInterestRate());
        loan.setLoanTermMonths(loanDetails.getLoanTermMonths());
        loan.setStatus(loanDetails.getStatus());
        loan.setLoanType(loanDetails.getLoanType());
        loan.setDescription(loanDetails.getDescription());
        
        Loan updatedLoan = loanRepository.save(loan);
        
        EntityModel<Loan> entityModel = EntityModel.of(updatedLoan,
                linkTo(methodOn(LoanController.class).getLoanById(id)).withSelfRel(),
                linkTo(methodOn(LoanController.class).getAllLoans()).withRel("loans"));
        
        return ResponseEntity.ok(entityModel);
    }
    
    /**
     * Delete a loan
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLoan(@PathVariable Long id) {
        if (!loanRepository.existsById(id)) {
            throw new RuntimeException("Loan not found with id: " + id);
        }
        
        loanRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Search loans by borrower name
     */
    @GetMapping("/search")
    public CollectionModel<EntityModel<Loan>> searchLoans(@RequestParam(required = false) String borrowerName,
                                                           @RequestParam(required = false) String status,
                                                           @RequestParam(required = false) String loanType) {
        List<Loan> loans;
        
        if (borrowerName != null && !borrowerName.isEmpty()) {
            loans = loanRepository.findByBorrowerNameContainingIgnoreCase(borrowerName);
        } else if (status != null && loanType != null) {
            loans = loanRepository.findByStatusAndLoanType(status, loanType);
        } else if (status != null) {
            loans = loanRepository.findByStatus(status);
        } else if (loanType != null) {
            loans = loanRepository.findByLoanType(loanType);
        } else {
            loans = loanRepository.findAll();
        }
        
        List<EntityModel<Loan>> loanModels = loans.stream()
                .map(loan -> EntityModel.of(loan,
                        linkTo(methodOn(LoanController.class).getLoanById(loan.getId())).withSelfRel(),
                        linkTo(methodOn(LoanController.class).getAllLoans()).withRel("loans")))
                .collect(Collectors.toList());
        
        return CollectionModel.of(loanModels,
                linkTo(methodOn(LoanController.class).getAllLoans()).withRel("loans"));
    }
    
    /**
     * Get loan statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getLoanStats() {
        List<Loan> allLoans = loanRepository.findAll();
        
        long totalLoans = allLoans.size();
        long pendingLoans = allLoans.stream().filter(l -> "PENDING".equals(l.getStatus())).count();
        long approvedLoans = allLoans.stream().filter(l -> "APPROVED".equals(l.getStatus())).count();
        double totalAmount = allLoans.stream()
                .mapToDouble(l -> l.getLoanAmount().doubleValue())
                .sum();
        
        return ResponseEntity.ok(new LoanStats(totalLoans, pendingLoans, approvedLoans, totalAmount));
    }
    
    // Inner class for statistics
    public static class LoanStats {
        private long totalLoans;
        private long pendingLoans;
        private long approvedLoans;
        private double totalAmount;
        
        public LoanStats(long totalLoans, long pendingLoans, long approvedLoans, double totalAmount) {
            this.totalLoans = totalLoans;
            this.pendingLoans = pendingLoans;
            this.approvedLoans = approvedLoans;
            this.totalAmount = totalAmount;
        }
        
        // Getters
        public long getTotalLoans() { return totalLoans; }
        public long getPendingLoans() { return pendingLoans; }
        public long getApprovedLoans() { return approvedLoans; }
        public double getTotalAmount() { return totalAmount; }
    }
}

