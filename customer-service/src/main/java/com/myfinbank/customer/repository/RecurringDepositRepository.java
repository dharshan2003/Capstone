package com.myfinbank.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myfinbank.customer.model.RecurringDeposit;

public interface RecurringDepositRepository extends JpaRepository<RecurringDeposit, Long> {

    List<RecurringDeposit> findByCustomerIdOrderByIdDesc(Long customerId);
}
