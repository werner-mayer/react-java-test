package com.example.loan.service;

import com.example.loan.model.dto.LoanRequest;
import com.example.loan.model.dto.LoanResponse;
import com.example.loan.model.dto.LoanRow;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LoanCalculatorServiceTest {

  private LoanRequest req(double valor, double taxa) {
    LoanRequest r = new LoanRequest();
    r.setStartDate(LocalDate.of(2024,1,1));
    r.setEndDate(LocalDate.of(2024,3,31));
    r.setFirstPaymentDate(LocalDate.of(2024,2,15));
    r.setLoanAmount(valor);
    r.setAnnualInterestRate(taxa);
    return r;
  }

  @Test
  void saldoDevedor_em_fechamento_sem_pagamento_inclui_juros() {
    LoanCalculatorService s = new LoanCalculatorService();
    LoanResponse resp = s.calculate(req(100000.0, 0.07));
    List<LoanRow> linhas = resp.getRows();
    LoanRow fimJan = linhas.stream().filter(l -> "2024-01-31".equals(l.getPeriodDate().toString())).findFirst().orElseThrow();
    assertEquals(583.33, fimJan.getInterestForPeriod(), 0.01);
    assertEquals(100583.33, fimJan.getOutstandingBalance(), 0.01);
  }

  @Test
  void pagamento_zera_juros_acumulado() {
    LoanCalculatorService s = new LoanCalculatorService();
    LoanResponse resp = s.calculate(req(100000.0, 0.07));
    LoanRow primeiroPag = resp.getRows().stream().filter(l -> l.getInstallmentAmount()!=null).findFirst().orElseThrow();
    assertEquals(0.0, primeiroPag.getAccruedInterest(), 0.001);
  }

  @Test
  void datas_invalidas_disparam_excecao() {
    LoanCalculatorService s = new LoanCalculatorService();
    LoanRequest r = new LoanRequest();
    r.setStartDate(LocalDate.of(2024,1,1));
    r.setEndDate(LocalDate.of(2023,12,31));
    r.setFirstPaymentDate(LocalDate.of(2024,2,15));
    r.setLoanAmount(1000.0);
    r.setAnnualInterestRate(0.1);
    assertThrows(IllegalArgumentException.class, () -> s.calculate(r));
  }
}
