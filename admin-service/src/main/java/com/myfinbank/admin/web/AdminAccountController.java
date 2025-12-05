package com.myfinbank.admin.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.myfinbank.admin.dto.AdminLoginRequest;
import com.myfinbank.admin.dto.AdminSignupRequest;
import com.myfinbank.admin.dto.AdminSummaryView;
import com.myfinbank.admin.service.AdminAccountService;

@RestController
@RequestMapping("/api/admins")
public class AdminAccountController {

    private final AdminAccountService accountService;

    public AdminAccountController(AdminAccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<AdminSummaryView> register(@RequestBody AdminSignupRequest request) {
        AdminSummaryView created = accountService.registerNewAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<AdminSummaryView> login(@RequestBody AdminLoginRequest request) {
        AdminSummaryView loggedIn = accountService.validateLogin(request);
        return ResponseEntity.ok(loggedIn);
    }

    @PostMapping("/{id}/logout")
    public ResponseEntity<Void> logout(@PathVariable Long id) {
        accountService.performLogout(id);
        return ResponseEntity.noContent().build();
    }
}
