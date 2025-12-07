package com.myfinbank.customer.repository;

import java.util.List; 
import java.util.Optional;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myfinbank.customer.model.BankAccount;
import com.myfinbank.customer.model.CustomerProfile;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByAccountNumber(String accountNumber);

    // use the relation field "owner"
    List<BankAccount> findByOwner(CustomerProfile owner);
    
    List<BankAccount> findByBalance(BigDecimal balance);


}
