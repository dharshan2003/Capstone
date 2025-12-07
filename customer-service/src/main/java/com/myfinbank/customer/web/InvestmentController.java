package com.myfinbank.customer.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.myfinbank.customer.dto.FixedDepositCreateRequest;
import com.myfinbank.customer.dto.RecurringDepositCreateRequest;
import com.myfinbank.customer.model.FixedDeposit;
import com.myfinbank.customer.model.RecurringDeposit;
import com.myfinbank.customer.service.FixedDepositService;
import com.myfinbank.customer.service.RecurringDepositService;

@RestController
@RequestMapping("/api/investments")
public class InvestmentController {

    private final FixedDepositService fdService;
    private final RecurringDepositService rdService;

    public InvestmentController(FixedDepositService fdService,
                                RecurringDepositService rdService) {
        this.fdService = fdService;
        this.rdService = rdService;
    }

    // ---------- Fixed Deposit ----------

    @PostMapping("/fd")
    public ResponseEntity<FixedDeposit> createFixedDeposit(
            @RequestBody FixedDepositCreateRequest request) {
        FixedDeposit created = fdService.createFixedDeposit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/fd/customer/{customerId}")
    public ResponseEntity<List<FixedDeposit>> getFDsForCustomer(
            @PathVariable Long customerId) {
        List<FixedDeposit> list = fdService.getFDsForCustomer(customerId);
        return ResponseEntity.ok(list);
    }

    // ---------- Recurring Deposit ----------

    @PostMapping("/rd")
    public ResponseEntity<RecurringDeposit> createRecurringDeposit(
            @RequestBody RecurringDepositCreateRequest request) {
        RecurringDeposit created = rdService.createRecurringDeposit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/rd/customer/{customerId}")
    public ResponseEntity<List<RecurringDeposit>> getRDsForCustomer(
            @PathVariable Long customerId) {
        List<RecurringDeposit> list = rdService.getRDsForCustomer(customerId);
        return ResponseEntity.ok(list);
    }
}
