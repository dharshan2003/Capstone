package com.myfinbank.admin.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.myfinbank.admin.dto.AdminAccountView;
import com.myfinbank.admin.dto.AdminCustomerView;
import com.myfinbank.admin.dto.AdminLoanView;
import com.myfinbank.admin.dto.ZeroBalanceAlertView;

@Service
public class AdminQueryService {

    private final RestClient customerRestClient;
    private final RestClient accountRestClient;
    private final RestClient loanRestClient;
    private final RestClient alertRestClient;

    public AdminQueryService(RestClient customerRestClient,
                             RestClient accountRestClient,
                             RestClient loanRestClient,
                             RestClient alertRestClient) {
        this.customerRestClient = customerRestClient;
        this.accountRestClient = accountRestClient;
        this.loanRestClient = loanRestClient;
        this.alertRestClient = alertRestClient;
    }

    // 1) Customers from customer-service
    public List<AdminCustomerView> fetchAllCustomers() {
        CustomerDto[] response = customerRestClient
                .get()
                .uri("/api/customers")
                .retrieve()
                .body(CustomerDto[].class);

        if (response == null) return List.of();

        return Arrays.stream(response)
                .map(c -> new AdminCustomerView(
                        c.getId(),
                        c.getFullName(),
                        c.getEmail(),
                        c.isActive()))
                .toList();
    }

    // 2) Accounts from account-service
    public List<AdminAccountView> fetchAllAccounts() {
        AccountDto[] response = accountRestClient
                .get()
                .uri("/api/accounts")
                .retrieve()
                .body(AccountDto[].class);

        if (response == null) return List.of();

        return Arrays.stream(response)
                .map(a -> new AdminAccountView(
                        a.getId(),
                        a.getCustomerId(),      // now exists
                        a.getCustomerName(),
                        a.getAccountNumber(),
                        a.getAccountType(),
                        a.getBalance()))
                .toList();
    }

    // 3) Pending loans from loan-service
    public List<AdminLoanView> fetchPendingLoans() {
        LoanDto[] response = loanRestClient
                .get()
                .uri("/api/loans/pending")
                .retrieve()
                .body(LoanDto[].class);

        if (response == null) return List.of();

        return Arrays.stream(response)
                .map(l -> new AdminLoanView(
                        l.getId(),
                        l.getCustomerName(),
                        l.getAmount(),
                        l.getType()))
                .toList();
    }

    // 4) Zero-balance alerts from alert-service
    public List<ZeroBalanceAlertView> fetchZeroBalanceAlerts() {
        AlertDto[] response = alertRestClient
                .get()
                .uri("/api/alerts/zero-balance")
                .retrieve()
                .body(AlertDto[].class);

        if (response == null) return List.of();

        return Arrays.stream(response)
                .map(a -> new ZeroBalanceAlertView(
                        a.getCustomerId(),
                        a.getCustomerName(),
                        a.getBalance()))
                .toList();
    }

    // 5) Customer activate/deactivate – customer-service
    public void setCustomerActive(Long id, boolean active) {
        String action = active ? "activate" : "deactivate";
        customerRestClient
                .post()
                .uri("/api/customers/{id}/" + action, id)
                .retrieve()
                .toBodilessEntity();
    }

    // 6) Approve / deny loans – loan-service
    public void approveLoan(Long id) {
        loanRestClient
                .post()
                .uri("/api/loans/{id}/approve", id)
                .retrieve()
                .toBodilessEntity();
    }

    public void denyLoan(Long id) {
        loanRestClient
                .post()
                .uri("/api/loans/{id}/deny", id)
                .retrieve()
                .toBodilessEntity();
    }

    // ----- DTOs matching each backend JSON -----

    public static class CustomerDto {
        private Long id;
        private String fullName;
        private String email;
        private boolean active;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }

    public static class AccountDto {
        private Long id;
        private Long customerId;           // added
        private String customerName;
        private String accountNumber;
        private String accountType;
        private double balance;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }

        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }

        public String getAccountNumber() { return accountNumber; }
        public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

        public String getAccountType() { return accountType; }
        public void setAccountType(String accountType) { this.accountType = accountType; }

        public double getBalance() { return balance; }
        public void setBalance(double balance) { this.balance = balance; }
    }

    public static class LoanDto {
        private Long id;
        private String customerName;
        private double amount;
        private String type;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    public static class AlertDto {
        private Long customerId;
        private String customerName;
        private double balance;

        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        public double getBalance() { return balance; }
        public void setBalance(double balance) { this.balance = balance; }
    }
}
