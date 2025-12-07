package com.myfinbank.admin.service;

import com.myfinbank.admin.dto.AdminCustomerView;
import java.util.List;

public interface CustomerService {

    List<AdminCustomerView> getAllCustomers();

    AdminCustomerView getCustomerById(Long id);

    AdminCustomerView updateStatus(Long id, boolean active);
}
