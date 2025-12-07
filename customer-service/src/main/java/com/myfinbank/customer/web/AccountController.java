package com.myfinbank.customer.web;

import java.util.List; 


import java.math.BigDecimal;
import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.myfinbank.customer.dto.AccountCreateRequest;
import com.myfinbank.customer.dto.AccountSummaryView;
import com.myfinbank.customer.service.AccountManagementService;
import com.myfinbank.customer.entity.Transaction;
import com.myfinbank.customer.entity.TransactionType;
import com.myfinbank.customer.dto.TransactionView;


@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountManagementService accountService;

    public AccountController(AccountManagementService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountSummaryView> create(@RequestBody AccountCreateRequest request) {
        AccountSummaryView created = accountService.openAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountSummaryView>> findByCustomer(@PathVariable Long customerId) {
        List<AccountSummaryView> list = accountService.getAccountsForCustomer(customerId);
        return ResponseEntity.ok(list);
    }

    

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<AccountSummaryView> deposit(
            @PathVariable String accountNumber,
            @RequestBody Map<String, BigDecimal> body) {

        BigDecimal amount = body.get("amount");
        AccountSummaryView view = accountService.depositToAccount(accountNumber, amount);
        return ResponseEntity.ok(view);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<AccountSummaryView> withdraw(
            @PathVariable String accountNumber,
            @RequestBody Map<String, BigDecimal> body) {

        BigDecimal amount = body.get("amount");
        AccountSummaryView view = accountService.withdrawFromAccount(accountNumber, amount);
        return ResponseEntity.ok(view);
    }
    @PostMapping("/transfer")
    public ResponseEntity<AccountSummaryView> transfer(@RequestBody Map<String, Object> body) {
        String fromAccount = (String) body.get("fromAccount");
        String toAccount = (String) body.get("toAccount");

        Object amtObj = body.get("amount");
        BigDecimal amount = null;
        if (amtObj instanceof Number) {
            amount = BigDecimal.valueOf(((Number) amtObj).doubleValue());
        }

        AccountSummaryView view =
                accountService.transferBetweenAccounts(fromAccount, toAccount, amount);

        return ResponseEntity.ok(view);
    }
    @GetMapping
    public ResponseEntity<List<AccountSummaryView>> getAllAccounts() {
        List<AccountSummaryView> list = accountService.getAllAccounts();
        return ResponseEntity.ok(list);
    }
 // inside AccountController
    @GetMapping("/transactions/{accountNumber}")
    public List<TransactionView> getByAccount(@PathVariable String accountNumber) {
        return accountService.getTransactionsForAccount(accountNumber);
    }
    




}
