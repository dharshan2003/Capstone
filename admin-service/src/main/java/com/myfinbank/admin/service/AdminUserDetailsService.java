package com.myfinbank.admin.service;

import com.myfinbank.admin.model.AdminUser;
import com.myfinbank.admin.repository.AdminUserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUserRepository repo;

    public AdminUserDetailsService(AdminUserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser admin = repo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
        return new User(
                admin.getEmail(),
                admin.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + admin.getRole()))
        );
    }
}
