package com.ullas.University.management.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.ullas.University.management.entity.Admin;
import com.ullas.University.management.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

class AdminServiceTest {
    private final AdminRepository repository = mock(AdminRepository.class);
    private final AccountService accounts = new AccountService(mock(PasswordEncoder.class));
    private final AdminService service = new AdminService(repository, new ModelMapper(), accounts);

    @Test
    void deleteRejectsTheLastAdmin() {
        Admin lastAdmin = admin(1L);
        when(repository.lockAll()).thenReturn(List.of(lastAdmin));

        assertThrows(ConflictException.class, () -> service.deleteAdmin(1L));

        verify(repository, never()).delete(lastAdmin);
        verify(repository, never()).flush();
    }

    @Test
    void deleteRemovesTargetWhenAnotherAdminRemains() {
        Admin target = admin(1L);
        when(repository.lockAll()).thenReturn(List.of(target, admin(2L)));
        when(repository.findById(1L)).thenReturn(Optional.of(target));

        service.deleteAdmin(1L);

        verify(repository).delete(target);
        verify(repository).flush();
    }

    private static Admin admin(Long id) {
        Admin admin = new Admin();
        admin.setId(id);
        return admin;
    }
}
