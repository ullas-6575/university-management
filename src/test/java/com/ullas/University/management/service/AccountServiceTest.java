package com.ullas.University.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.ullas.University.management.dto.AdminDTO;
import com.ullas.University.management.entity.AppUser;
import com.ullas.University.management.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class AccountServiceTest {
    private final PasswordEncoder encoder = mock(PasswordEncoder.class);
    private final AccountService service = new AccountService(encoder);

    @Test
    void createNormalizesEmailAndEncodesPassword() {
        AdminDTO dto = accountDto();
        dto.setEmail("  ADMIN@EXAMPLE.TEST ");
        when(encoder.encode("Valid-password-123")).thenReturn("encoded-password");

        AppUser account = service.create(dto, Role.ADMIN);

        assertEquals("admin", account.getUsername());
        assertEquals("admin@example.test", account.getEmail());
        assertEquals("encoded-password", account.getPasswordHash());
        assertEquals(Role.ADMIN, account.getRole());
        verify(encoder).encode("Valid-password-123");
    }

    @Test
    void updateRejectsUsernameChanges() {
        AppUser account = new AppUser("admin", "admin@example.test", "existing-hash", Role.ADMIN);
        AdminDTO dto = accountDto();
        dto.setUsername("changed-admin");

        assertThrows(IllegalArgumentException.class, () -> service.update(account, dto));

        assertEquals("admin@example.test", account.getEmail());
        assertEquals("existing-hash", account.getPasswordHash());
        verifyNoInteractions(encoder);
    }

    @Test
    void updateNormalizesEmailAndEncodesReplacementPassword() {
        AppUser account = new AppUser("admin", "admin@example.test", "existing-hash", Role.ADMIN);
        AdminDTO dto = accountDto();
        dto.setEmail("  NEW@EXAMPLE.TEST ");
        when(encoder.encode("Valid-password-123")).thenReturn("replacement-hash");

        service.update(account, dto);

        assertEquals("new@example.test", account.getEmail());
        assertEquals("replacement-hash", account.getPasswordHash());
        assertEquals(1, account.getAuthVersion());
    }

    @Test
    void updateWithoutPasswordPreservesExistingHash() {
        AppUser account = new AppUser("admin", "admin@example.test", "existing-hash", Role.ADMIN);
        AdminDTO dto = accountDto();
        dto.setPassword(null);
        dto.setUsername(null);

        service.update(account, dto);

        assertEquals("existing-hash", account.getPasswordHash());
        assertEquals(0, account.getAuthVersion());
        verifyNoInteractions(encoder);
    }

    private static AdminDTO accountDto() {
        AdminDTO dto = new AdminDTO();
        dto.setName("Test Admin");
        dto.setOffice("Administration");
        dto.setUsername("admin");
        dto.setEmail("admin@example.test");
        dto.setPassword("Valid-password-123");
        return dto;
    }
}
