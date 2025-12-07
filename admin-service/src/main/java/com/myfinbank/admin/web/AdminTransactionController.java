package com.myfinbank.admin.web;

import com.myfinbank.admin.dto.AdminTransactionView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminTransactionController {

    private final RestTemplate restTemplate;

    public AdminTransactionController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/accounts/{accountNumber}/transactions")
    public List<AdminTransactionView> getTransactions(@PathVariable String accountNumber) {
        AdminTransactionView[] arr = restTemplate.getForObject(
                "http://localhost:8081/api/accounts/transactions/" + accountNumber,
                AdminTransactionView[].class
        );
        return Arrays.asList(arr);
    }
}
