package com.myfinbank.admin.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myfinbank.admin.dto.AdminAccountView;
import com.myfinbank.admin.dto.AdminCustomerView;
import com.myfinbank.admin.dto.AdminLoanView;
import com.myfinbank.admin.dto.ZeroBalanceAlertView;
import com.myfinbank.admin.service.AdminQueryService;

@RestController
@RequestMapping("/api/admins")
public class AdminDashboardController {

    private final AdminQueryService adminQueryService;

    public AdminDashboardController(AdminQueryService adminQueryService) {
        this.adminQueryService = adminQueryService;
    }

    // 1) Customers for dashboard
    @GetMapping("/customers")
    public List<AdminCustomerView> listCustomers() {
        return adminQueryService.fetchAllCustomers();
    }

    // 2) Accounts for dashboard
    @GetMapping("/accounts")
    public List<AdminAccountView> listAccounts() {
        return adminQueryService.fetchAllAccounts();
    }

    // 3) Pending loans
    @GetMapping("/loans/pending")
    public List<AdminLoanView> listPendingLoans() {
        return adminQueryService.fetchPendingLoans();
    }

    // 4) Zero-balance alerts
    @GetMapping("/alerts/zero-balance")
    public List<ZeroBalanceAlertView> listZeroBalanceAlerts() {
        return adminQueryService.fetchZeroBalanceAlerts();
    }

    // 5) Activate / deactivate customer
    @PostMapping("/customers/{id}/activate")
    public void activateCustomer(@PathVariable Long id) {
        adminQueryService.setCustomerActive(id, true);
    }

    @PostMapping("/customers/{id}/deactivate")
    public void deactivateCustomer(@PathVariable Long id) {
        adminQueryService.setCustomerActive(id, false);
    }

    // 6) Approve / deny loan
    @PostMapping("/loans/{id}/approve")
    public void approveLoan(@PathVariable Long id) {
        adminQueryService.approveLoan(id);
    }

    @PostMapping("/loans/{id}/deny")
    public void denyLoan(@PathVariable Long id) {
        adminQueryService.denyLoan(id);
    }
}
