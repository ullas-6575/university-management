package com.ullas.University.management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 40, updatable = false)
    private String username;
    @Column(nullable = false, unique = true, length = 254)
    private String email;
    @Column(nullable = false, length = 100)
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10, updatable = false)
    private Role role;
    @Column(nullable = false)
    private long authVersion;

    protected AppUser() {
    }

    public AppUser(String username, String email, String passwordHash, Role role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public long getAuthVersion() {
        return authVersion;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void changePassword(String hash) {
        this.passwordHash = hash;
        this.authVersion++;
    }
}
