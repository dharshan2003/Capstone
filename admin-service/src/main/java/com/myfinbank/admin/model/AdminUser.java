package com.myfinbank.admin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admins")
@Getter
@Setter
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;   // encoded

    @Column(nullable = false)
    private String role;       // e.g. "ADMIN"

    @Column(nullable = true)
    private String displayName;

    @Column(nullable = true, unique = true)
    private String loginName;
}
