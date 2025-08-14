package com.example.loan.service;

import com.example.loan.model.dto.LoanRequest;
import com.example.loan.model.dto.LoanResponse;
import com.example.loan.model.dto.LoanRow;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

@Service
public class LoanCalculatorService {
  private static final int DAY_COUNT_BASE = 360;

  public LoanResponse calculate(LoanRequest request) {
    LocalDate startDate = request.getStartDate();
    LocalDate endDate = request.getEndDate();
    LocalDate firstPaymentDate = request.getFirstPaymentDate();
    validateDates(startDate, endDate, firstPaymentDate);

    List<LocalDate> paymentDates = generatePaymentDates(firstPaymentDate, endDate);
    int installmentCount = paymentDates.size();

    double outstandingPrincipal = request.getLoanAmount();
    double constantPrincipalAmortization = outstandingPrincipal / installmentCount;

    TreeSet<LocalDate> timelineDates = new TreeSet<>();
    timelineDates.add(startDate);
    timelineDates.addAll(monthEndsBetween(startDate, endDate));
    timelineDates.addAll(paymentDates);

    List<LoanRow> rows = new ArrayList<>();
    LocalDate previousDate = startDate;
    double accruedInterestSinceLastPayment = 0.0;
    int installmentNumber = 0;

    LoanRow first = new LoanRow();
    first.setPeriodDate(startDate);
    first.setLoanAmount(round2(outstandingPrincipal));
    first.setOutstandingBalance(round2(outstandingPrincipal));
    first.setInstallmentLabel(null);
    first.setInstallmentAmount(0.0);
    first.setPrincipalAmortization(0.0);
    first.setBalanceAfterPayment(null);
    first.setInterestForPeriod(0.0);
    first.setAccruedInterest(0.0);
    first.setAmountPaid(0.0);
    rows.add(first);

    for (LocalDate currentDate : timelineDates.tailSet(startDate, false)) {
      long daysBetween = Duration.between(previousDate.atStartOfDay(), currentDate.atStartOfDay()).toDays();

      double annualRate = request.getAnnualInterestRate() / 100.0;
      double baseForInterest = outstandingPrincipal + accruedInterestSinceLastPayment;
      double periodFactor = Math.pow(1.0 + annualRate, (double) daysBetween / DAY_COUNT_BASE) - 1.0;
      double interestForPeriod = baseForInterest * periodFactor;
      accruedInterestSinceLastPayment += interestForPeriod;

      LoanRow row = new LoanRow();
      row.setPeriodDate(currentDate);
      row.setLoanAmount(0.0);
      row.setInterestForPeriod(round2(interestForPeriod));

      boolean isPaymentDate = paymentDates.contains(currentDate);
      double displayedOutstandingBalance;

      if (isPaymentDate) {
        installmentNumber++;
        double principalAmortization = (installmentNumber == installmentCount) ? outstandingPrincipal : constantPrincipalAmortization;
        double interestPaid = accruedInterestSinceLastPayment;
        double installmentAmount = principalAmortization + interestPaid;
        double balanceAfterPayment = outstandingPrincipal - principalAmortization;

        row.setInstallmentLabel(installmentNumber + "/" + installmentCount);
        row.setInstallmentAmount(round2(installmentAmount));
        row.setPrincipalAmortization(round2(principalAmortization));
        row.setBalanceAfterPayment(round2(balanceAfterPayment));
        row.setAmountPaid(round2(interestPaid));
        row.setAccruedInterest(0.0);

        displayedOutstandingBalance = balanceAfterPayment;
        outstandingPrincipal = balanceAfterPayment;
        accruedInterestSinceLastPayment = 0.0;
      } else {
        row.setInstallmentLabel(null);
        row.setInstallmentAmount(0.0);
        row.setPrincipalAmortization(0.0);
        row.setBalanceAfterPayment(null);
        row.setAmountPaid(0.0);
        row.setAccruedInterest(round2(accruedInterestSinceLastPayment));

        displayedOutstandingBalance = outstandingPrincipal + accruedInterestSinceLastPayment;
      }

      row.setOutstandingBalance(round2(displayedOutstandingBalance));
      rows.add(row);
      previousDate = currentDate;
    }

    return new LoanResponse(installmentCount, rows);
  }

  private void validateDates(LocalDate startDate, LocalDate endDate, LocalDate firstPaymentDate) {
    if (startDate == null || endDate == null || firstPaymentDate == null) {
      throw new IllegalArgumentException("Dates must not be null.");
    }
    if (!endDate.isAfter(startDate)) throw new IllegalArgumentException("End date must be after start date.");
    if (!firstPaymentDate.isAfter(startDate) || !firstPaymentDate.isBefore(endDate)) {
      throw new IllegalArgumentException("First payment date must be > start date and < end date.");
    }
  }

  private List<LocalDate> generatePaymentDates(LocalDate firstPaymentDate, LocalDate endDate) {
    List<LocalDate> dates = new ArrayList<>();
    boolean eom = isEndOfMonth(firstPaymentDate);
    int originalDom = firstPaymentDate.getDayOfMonth();

    LocalDate current = firstPaymentDate;
    while (!current.isAfter(endDate)) {
      dates.add(current);
      current = nextPaymentDate(current, eom, originalDom);
    }
    if (!dates.contains(endDate)) {
      dates.add(endDate);
      dates.sort(Comparator.naturalOrder());
    }
    return dates;
  }

  private boolean isEndOfMonth(LocalDate d) {
    return d.getDayOfMonth() == d.lengthOfMonth();
  }

  private LocalDate nextPaymentDate(LocalDate current, boolean eom, int originalDom) {
    LocalDate next = current.plusMonths(1);
    if (eom) return next.with(TemporalAdjusters.lastDayOfMonth());
    int last = next.lengthOfMonth();
    int dom = Math.min(originalDom, last);
    return next.withDayOfMonth(dom);
  }

  private List<LocalDate> monthEndsBetween(LocalDate startDate, LocalDate endDate) {
    List<LocalDate> out = new ArrayList<>();
    LocalDate cursor = startDate.with(TemporalAdjusters.lastDayOfMonth());
    if (cursor.isEqual(startDate)) cursor = cursor.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
    while (cursor.isBefore(endDate)) {
      out.add(cursor);
      cursor = cursor.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
    }
    return out;
  }

  private double round2(double value) {
    return Math.round(value * 100.0) / 100.0;
  }
}
