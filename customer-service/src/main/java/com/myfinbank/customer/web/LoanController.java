package com.myfinbank.customer.web;

import java.util.List;
import com.myfinbank.customer.dto.PendingLoanView;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.myfinbank.customer.dto.LoanApplyRequest;
import com.myfinbank.customer.model.LoanApplication;
import com.myfinbank.customer.service.LoanManagementService;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanManagementService loanService;

    public LoanController(LoanManagementService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/apply")
    public ResponseEntity<LoanApplication> apply(@RequestBody LoanApplyRequest request) {
        LoanApplication created = loanService.applyForLoan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<LoanApplication>> getLoansForCustomer(@PathVariable Long customerId) {
        List<LoanApplication> loans = loanService.getLoansForCustomer(customerId);
        return ResponseEntity.ok(loans);
    }

    @PatchMapping("/{loanId}/decision")
    public ResponseEntity<LoanApplication> decide(@PathVariable Long loanId) {
        LoanApplication updated = loanService.decideLoan(loanId);
        return ResponseEntity.ok(updated);
    }
 // customer-service

    @GetMapping("/pending")
    public ResponseEntity<List<PendingLoanView>> getPendingLoans() {
        List<PendingLoanView> loans = loanService.getPendingLoans();   // implement this
        return ResponseEntity.ok(loans);
    }

    @PostMapping("/{loanId}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long loanId) {
        loanService.approveLoan(loanId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{loanId}/deny")
    public ResponseEntity<Void> deny(@PathVariable Long loanId) {
        loanService.denyLoan(loanId);
        return ResponseEntity.ok().build();
    }

}
