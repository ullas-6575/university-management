package com.ullas.University.management.config;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import com.ullas.University.management.entity.AppUser;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public final class CampusPrincipal implements UserDetails, CredentialsContainer {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Long id;
    private final String username;
    private String password;
    private final String role;
    private final long authVersion;

    public CampusPrincipal(AppUser user) {
        id = user.getId();
        username = user.getUsername();
        password = user.getPasswordHash();
        role = user.getRole().name();
        authVersion = user.getAuthVersion();
    }

    public Long id() {
        return id;
    }

    public long authVersion() {
        return authVersion;
    }

    public String role() {
        return role;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void eraseCredentials() {
        password = null;
    }
}
