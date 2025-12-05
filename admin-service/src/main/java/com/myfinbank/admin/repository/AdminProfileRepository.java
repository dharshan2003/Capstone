package com.myfinbank.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myfinbank.admin.model.AdminProfile;

public interface AdminProfileRepository extends JpaRepository<AdminProfile, Long> {

    Optional<AdminProfile> findByLoginName(String loginName);

    boolean existsByEmail(String email);

    boolean existsByLoginName(String loginName);
}
