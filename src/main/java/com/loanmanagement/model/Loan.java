package com.loanmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Borrower name is required")
    @Size(min = 2, max = 100, message = "Borrower name must be between 2 and 100 characters")
    private String borrowerName;
    
    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "100.0", message = "Loan amount must be at least 100")
    @DecimalMax(value = "1000000.0", message = "Loan amount cannot exceed 1,000,000")
    private BigDecimal loanAmount;
    
    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.0", message = "Interest rate cannot be negative")
    @DecimalMax(value = "30.0", message = "Interest rate cannot exceed 30%")
    private BigDecimal interestRate;
    
    @NotNull(message = "Loan term is required")
    @Min(value = 1, message = "Loan term must be at least 1 month")
    @Max(value = 360, message = "Loan term cannot exceed 360 months")
    private Integer loanTermMonths;
    
    @NotNull(message = "Loan date is required")
    private LocalDate loanDate;
    
    private String status; // PENDING, APPROVED, REJECTED, PAID_OFF
    
    private String loanType; // PERSONAL, BUSINESS, MORTGAGE, AUTO
    
    private String description;
    
    // Constructors
    public Loan() {
        this.status = "PENDING";
        this.loanDate = LocalDate.now();
    }
    
    public Loan(String borrowerName, BigDecimal loanAmount, BigDecimal interestRate, 
                Integer loanTermMonths, String loanType) {
        this();
        this.borrowerName = borrowerName;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.loanTermMonths = loanTermMonths;
        this.loanType = loanType;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getBorrowerName() {
        return borrowerName;
    }
    
    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }
    
    public BigDecimal getLoanAmount() {
        return loanAmount;
    }
    
    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }
    
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    
    public Integer getLoanTermMonths() {
        return loanTermMonths;
    }
    
    public void setLoanTermMonths(Integer loanTermMonths) {
        this.loanTermMonths = loanTermMonths;
    }
    
    public LocalDate getLoanDate() {
        return loanDate;
    }
    
    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getLoanType() {
        return loanType;
    }
    
    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    // Helper method to calculate monthly payment
    public BigDecimal calculateMonthlyPayment() {
        if (loanAmount == null || interestRate == null || loanTermMonths == null) {
            return BigDecimal.ZERO;
        }
        
        double monthlyRate = interestRate.doubleValue() / 100 / 12;
        double principal = loanAmount.doubleValue();
        int months = loanTermMonths;
        
        if (monthlyRate == 0) {
            return BigDecimal.valueOf(principal / months);
        }
        
        double monthlyPayment = principal * (monthlyRate * Math.pow(1 + monthlyRate, months)) 
                                / (Math.pow(1 + monthlyRate, months) - 1);
        
        return BigDecimal.valueOf(monthlyPayment).setScale(2, java.math.RoundingMode.HALF_UP);
    }
}

