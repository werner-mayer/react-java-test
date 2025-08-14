package com.example.loan.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class LoanRequest {
  @NotNull private LocalDate startDate;
  @NotNull private LocalDate endDate;
  @NotNull private LocalDate firstPaymentDate;
  @Positive @NotNull private Double loanAmount;
  @Positive @NotNull private Double annualInterestRate;

  public LocalDate getStartDate() { return startDate; }
  public void setStartDate(LocalDate v) { this.startDate = v; }
  public LocalDate getEndDate() { return endDate; }
  public void setEndDate(LocalDate v) { this.endDate = v; }
  public LocalDate getFirstPaymentDate() { return firstPaymentDate; }
  public void setFirstPaymentDate(LocalDate v) { this.firstPaymentDate = v; }
  public Double getLoanAmount() { return loanAmount; }
  public void setLoanAmount(Double v) { this.loanAmount = v; }
  public Double getAnnualInterestRate() { return annualInterestRate; }
  public void setAnnualInterestRate(Double v) { this.annualInterestRate = v; }
}
