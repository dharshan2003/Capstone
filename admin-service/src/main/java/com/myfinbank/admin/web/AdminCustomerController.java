package com.myfinbank.admin.web;

import com.myfinbank.admin.dto.AdminCustomerView;
import com.myfinbank.admin.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/customers")
public class AdminCustomerController {

    private final CustomerService customerService;

    public AdminCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // list for admin dashboard
    @GetMapping
    public List<AdminCustomerView> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // view single customer (for View page)
    @GetMapping("/{id}")
    public AdminCustomerView getCustomer(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    // deactivate / activate (for Deactivate button)
    @PutMapping("/{id}/status")
    public AdminCustomerView updateStatus(@PathVariable Long id,
                                          @RequestParam boolean active) {
        return customerService.updateStatus(id, active);
    }
}
