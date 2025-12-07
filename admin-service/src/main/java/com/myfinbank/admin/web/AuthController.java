package com.myfinbank.admin.web;

import com.myfinbank.admin.model.AdminUser;
import com.myfinbank.admin.repository.AdminUserRepository;
import com.myfinbank.admin.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final AdminUserRepository adminRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager,
                          JwtService jwtService,
                          AdminUserRepository adminRepo,
                          PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Email and password are required"
            ));
        }

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            AdminUser admin = adminRepo.findByEmail(email).orElseThrow();
            String token = jwtService.generateToken(email);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "adminId", admin.getId(),
                    "displayName", admin.getDisplayName() != null ? admin.getDisplayName() : email
            ));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message", "Invalid credentials"
            ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String displayName = body.getOrDefault("displayName", email);
        String loginName = body.getOrDefault("loginName", email);

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Email and password are required"
            ));
        }

        Optional<AdminUser> existing = adminRepo.findByEmail(email);
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "message", "Email already registered"
            ));
        }

        AdminUser admin = new AdminUser();
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole("ADMIN");
        admin.setDisplayName(displayName);
        admin.setLoginName(loginName);

        AdminUser saved = adminRepo.save(admin);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Registered",
                "adminId", saved.getId(),
                "displayName", saved.getDisplayName() != null ? saved.getDisplayName() : saved.getEmail()
        ));
    }
}
