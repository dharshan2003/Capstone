package com.myfinbank.customer.web;

import java.util.List;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.myfinbank.customer.dto.CustomerLoginRequest;
import com.myfinbank.customer.dto.CustomerSignupRequest;
import com.myfinbank.customer.dto.CustomerSummaryView;
import com.myfinbank.customer.service.CustomerAccountService;
import com.myfinbank.customer.dto.CustomerLoginResponse;

@RestController
@RequestMapping("/api/customers")
public class CustomerAccountController {

    private final CustomerAccountService accountService;

    public CustomerAccountController(CustomerAccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerSummaryView> register(@RequestBody CustomerSignupRequest request) {
        CustomerSummaryView created = accountService.registerNewCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomerLoginResponse> login(@RequestBody CustomerLoginRequest request) {
        CustomerSummaryView loggedIn = accountService.validateLogin(request);

        CustomerLoginResponse resp = new CustomerLoginResponse();
        resp.setCustomerId(loggedIn.getId());
        resp.setDisplayName(loggedIn.getFullName());
        // resp.setToken(jwtService.generateToken(...)); // if you use JWT

        return ResponseEntity.ok(resp);
    }


    // NEW: list all customers for admin dashboard
    @GetMapping
    public ResponseEntity<List<CustomerSummaryView>> getAllCustomers() {
        List<CustomerSummaryView> customers = accountService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
 // PUT /api/customers/{id}/status?active=false
    @PutMapping("/{id}/status")
    public ResponseEntity<CustomerSummaryView> updateStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {

        CustomerSummaryView updated = accountService.updateStatus(id, active);
        return ResponseEntity.ok(updated);
    }
 // GET /api/customers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CustomerSummaryView> getCustomerById(@PathVariable Long id) {
        CustomerSummaryView view = accountService.getCustomerById(id);
        return ResponseEntity.ok(view);
    }


}
