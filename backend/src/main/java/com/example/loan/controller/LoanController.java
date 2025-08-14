package com.example.loan.controller;

import com.example.loan.model.dto.LoanRequest;
import com.example.loan.model.dto.LoanResponse;
import com.example.loan.service.LoanCalculatorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8085"})
public class LoanController {
  private final LoanCalculatorService service;

  public LoanController(LoanCalculatorService service) {
    this.service = service;
  }

  @PostMapping("/calculate")
  public ResponseEntity<LoanResponse> calculate(@Valid @RequestBody LoanRequest request) {
    return ResponseEntity.ok(service.calculate(request));
  }
}
