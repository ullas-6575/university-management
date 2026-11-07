package com.ullas.University.management.service;

import java.util.Locale;
import com.ullas.University.management.dto.PersonDTO;
import com.ullas.University.management.entity.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final PasswordEncoder encoder;

    public AccountService(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public AppUser create(PersonDTO dto, Role role) {
        return new AppUser(dto.getUsername(), dto.getEmail().strip().toLowerCase(Locale.ROOT),
                encoder.encode(dto.getPassword()), role);
    }

    public void update(AppUser account, PersonDTO dto) {
        if (dto.getUsername() != null && !account.getUsername().equals(dto.getUsername()))
            throw new IllegalArgumentException("Username is immutable");
        account.setEmail(dto.getEmail().strip().toLowerCase(Locale.ROOT));
        if (dto.getPassword() != null)
            account.changePassword(encoder.encode(dto.getPassword()));
    }
}
