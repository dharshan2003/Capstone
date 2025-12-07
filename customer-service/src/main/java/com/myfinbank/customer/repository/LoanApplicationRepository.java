package com.myfinbank.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myfinbank.customer.model.LoanApplication;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

   
    List<LoanApplication> findByStatusOrderByIdDesc(String status);

    
    List<LoanApplication> findByCustomerIdOrderByIdDesc(Long customerId);
}
