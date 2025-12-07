package com.myfinbank.customer.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myfinbank.customer.dto.CustomerLoginRequest;
import com.myfinbank.customer.dto.CustomerSignupRequest;
import com.myfinbank.customer.dto.CustomerSummaryView;
import com.myfinbank.customer.exception.CustomerRegistrationException;
import com.myfinbank.customer.exception.InvalidLoginException;
import com.myfinbank.customer.model.CustomerProfile;
import com.myfinbank.customer.repository.CustomerProfileRepository;

@Service
public class CustomerAccountService {

    private final CustomerProfileRepository customerRepo;

    public CustomerAccountService(CustomerProfileRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Transactional
    public CustomerSummaryView registerNewCustomer(CustomerSignupRequest request) {

        if (customerRepo.existsByEmail(request.getEmail())) {
            throw new CustomerRegistrationException("Email is already registered");
        }
        if (customerRepo.existsByLoginName(request.getLoginName())) {
            throw new CustomerRegistrationException("Login name is already taken");
        }

        CustomerProfile profile = new CustomerProfile();
        profile.setFullName(request.getFullName());
        profile.setEmail(request.getEmail());
        profile.setMobile(request.getMobile());
        profile.setLoginName(request.getLoginName());
        profile.setLoginSecret(request.getLoginSecret());
        profile.setActive(true);

        CustomerProfile saved = customerRepo.save(profile);

        CustomerSummaryView view = new CustomerSummaryView();
        view.setId(saved.getId());
        view.setFullName(saved.getFullName());
        view.setEmail(saved.getEmail());
        view.setActive(saved.isActive());
        return view;
    }

    public CustomerSummaryView validateLogin(CustomerLoginRequest request) {
        Optional<CustomerProfile> found = customerRepo.findByLoginName(request.getLoginName());
        CustomerProfile profile = found
                .filter(p -> p.isActive()
                        && p.getLoginSecret().equals(request.getLoginSecret()))
                .orElseThrow(() -> new InvalidLoginException("Invalid credentials"));

        CustomerSummaryView view = new CustomerSummaryView();
        view.setId(profile.getId());
        view.setFullName(profile.getFullName());
        view.setEmail(profile.getEmail());
        view.setActive(profile.isActive());
        return view;
    }

    @Transactional(readOnly = true)
    public List<CustomerSummaryView> getAllCustomers() {
        return customerRepo.findAll()
                .stream()
                .map(profile -> {
                    CustomerSummaryView view = new CustomerSummaryView();
                    view.setId(profile.getId());
                    view.setFullName(profile.getFullName());
                    view.setEmail(profile.getEmail());
                    view.setActive(profile.isActive());
                    return view;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void performLogout(Long customerId) {
        // placeholder: JWT-based logout can be added later
    }

    // NEW: activate / deactivate a customer
    @Transactional
    public CustomerSummaryView updateStatus(Long id, boolean active) {
        CustomerProfile customer = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found " + id));

        customer.setActive(active);
        CustomerProfile saved = customerRepo.save(customer);

        CustomerSummaryView view = new CustomerSummaryView();
        view.setId(saved.getId());
        view.setFullName(saved.getFullName());
        view.setEmail(saved.getEmail());
        view.setActive(saved.isActive());
        return view;
    }
    @Transactional(readOnly = true)
    public CustomerSummaryView getCustomerById(Long id) {
        CustomerProfile profile = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found " + id));

        CustomerSummaryView view = new CustomerSummaryView();
        view.setId(profile.getId());
        view.setFullName(profile.getFullName());
        view.setEmail(profile.getEmail());
        view.setActive(profile.isActive());
        return view;
    }

}
