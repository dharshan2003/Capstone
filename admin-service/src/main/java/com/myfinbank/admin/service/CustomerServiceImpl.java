package com.myfinbank.admin.service;

import com.myfinbank.admin.dto.AdminCustomerView;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final RestTemplate restTemplate;
    private final String customerServiceBaseUrl = "http://localhost:8081/api/customers";

    public CustomerServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<AdminCustomerView> getAllCustomers() {
        AdminCustomerView[] response =
                restTemplate.getForObject(customerServiceBaseUrl, AdminCustomerView[].class);
        return Arrays.asList(response);
    }

    @Override
    public AdminCustomerView getCustomerById(Long id) {
        return restTemplate.getForObject(
                customerServiceBaseUrl + "/" + id, AdminCustomerView.class);
    }

    @Override
    public AdminCustomerView updateStatus(Long id, boolean active) {
        String url = customerServiceBaseUrl + "/" + id + "/status?active=" + active;
        restTemplate.put(url, null);
        return getCustomerById(id);
    }
}
