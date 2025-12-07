package com.myfinbank.admin.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myfinbank.admin.dto.AdminLoginRequest;
import com.myfinbank.admin.dto.AdminSignupRequest;
import com.myfinbank.admin.dto.AdminSummaryView;
import com.myfinbank.admin.model.AdminProfile;
import com.myfinbank.admin.repository.AdminProfileRepository;

@Service
public class AdminAccountService {

    private final AdminProfileRepository adminRepo;

    public AdminAccountService(AdminProfileRepository adminRepo) {
        this.adminRepo = adminRepo;
    }

    @Transactional
    public AdminSummaryView registerNewAdmin(AdminSignupRequest request) {
        if (adminRepo.existsByEmail(request.getEmail())
                || adminRepo.existsByLoginName(request.getLoginName())) {
            throw new IllegalStateException("Admin with same email or login already exists");
        }

        AdminProfile profile = new AdminProfile();
        profile.setDisplayName(request.getDisplayName());
        profile.setEmail(request.getEmail());
        profile.setLoginName(request.getLoginName());
        profile.setLoginSecret(request.getLoginSecret());
        profile.setActive(true);

        AdminProfile saved = adminRepo.save(profile);

        AdminSummaryView view = new AdminSummaryView();
        view.setId(saved.getId());
        view.setDisplayName(saved.getDisplayName());
        view.setEmail(saved.getEmail());
        view.setActive(saved.isActive());
        return view;
    }

    @Transactional(readOnly = true)
    public AdminSummaryView validateLogin(AdminLoginRequest request) {
        // Find by login name
        AdminProfile profile = adminRepo.findByLoginName(request.getLoginName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid admin credentials"));

        // Check password
        if (!profile.getLoginSecret().equals(request.getLoginSecret())) {
            throw new IllegalArgumentException("Invalid admin credentials");
        }

        // Optional: check active flag
        if (!profile.isActive()) {
            throw new IllegalArgumentException("Admin is inactive");
        }

        // Build summary view
        AdminSummaryView view = new AdminSummaryView();
        view.setId(profile.getId());
        view.setDisplayName(profile.getDisplayName());
        view.setEmail(profile.getEmail());
        view.setActive(profile.isActive());
        return view;
    }

    @Transactional
    public void performLogout(Long adminId) {
        // placeholder for future JWT-based logout
    }
}
