package com.myfinbank.admin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin_profile")
public class AdminProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "email_id", nullable = false, unique = true)
    private String email;

    @Column(name = "login_name", nullable = false, unique = true)
    private String loginName;

    @Column(name = "login_secret", nullable = false)
    private String loginSecret;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLoginName() { return loginName; }
    public void setLoginName(String loginName) { this.loginName = loginName; }

    public String getLoginSecret() { return loginSecret; }
    public void setLoginSecret(String loginSecret) { this.loginSecret = loginSecret; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
