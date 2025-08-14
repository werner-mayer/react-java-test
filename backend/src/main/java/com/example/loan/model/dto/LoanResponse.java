package com.example.loan.model.dto;

import java.util.List;

public class LoanResponse {
  private int installmentCount;
  private int dayCountBase = 360;
  private List<LoanRow> rows;

  public LoanResponse(int count, List<LoanRow> rows) {
    this.installmentCount = count;
    this.rows = rows;
  }

  public int getInstallmentCount() { return installmentCount; }
  public int getDayCountBase() { return dayCountBase; }
  public List<LoanRow> getRows() { return rows; }
}
