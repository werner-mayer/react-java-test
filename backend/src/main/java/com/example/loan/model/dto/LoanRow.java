package com.example.loan.model.dto;

import java.time.LocalDate;

public class LoanRow {
  private LocalDate periodDate;
  private Double loanAmount;
  private Double outstandingBalance;
  private String installmentLabel;
  private Double installmentAmount;
  private Double principalAmortization;
  private Double balanceAfterPayment;
  private Double interestForPeriod;
  private Double accruedInterest;
  private Double amountPaid;

  public LocalDate getPeriodDate() { return periodDate; }
  public void setPeriodDate(LocalDate d) { this.periodDate = d; }
  public Double getLoanAmount() { return loanAmount; }
  public void setLoanAmount(Double v) { this.loanAmount = v; }
  public Double getOutstandingBalance() { return outstandingBalance; }
  public void setOutstandingBalance(Double v) { this.outstandingBalance = v; }
  public String getInstallmentLabel() { return installmentLabel; }
  public void setInstallmentLabel(String v) { this.installmentLabel = v; }
  public Double getInstallmentAmount() { return installmentAmount; }
  public void setInstallmentAmount(Double v) { this.installmentAmount = v; }
  public Double getPrincipalAmortization() { return principalAmortization; }
  public void setPrincipalAmortization(Double v) { this.principalAmortization = v; }
  public Double getBalanceAfterPayment() { return balanceAfterPayment; }
  public void setBalanceAfterPayment(Double v) { this.balanceAfterPayment = v; }
  public Double getInterestForPeriod() { return interestForPeriod; }
  public void setInterestForPeriod(Double v) { this.interestForPeriod = v; }
  public Double getAccruedInterest() { return accruedInterest; }
  public void setAccruedInterest(Double v) { this.accruedInterest = v; }
  public Double getAmountPaid() { return amountPaid; }
  public void setAmountPaid(Double v) { this.amountPaid = v; }
}
