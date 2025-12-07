package com.myfinbank.customer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myfinbank.customer.model.CustomerProfile;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {

    Optional<CustomerProfile> findByLoginName(String loginName);

    boolean existsByEmail(String email);

    boolean existsByLoginName(String loginName);
}
