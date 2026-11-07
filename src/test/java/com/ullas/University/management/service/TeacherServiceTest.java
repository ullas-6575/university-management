package com.ullas.University.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ullas.University.management.dto.TeacherDTO;
import com.ullas.University.management.entity.Teacher;
import com.ullas.University.management.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

class TeacherServiceTest {
    private final TeacherRepository repository = mock(TeacherRepository.class);
    private final PasswordEncoder encoder = mock(PasswordEncoder.class);
    private final AccountService accounts = new AccountService(encoder);
    private final TeacherService service = new TeacherService(repository, new ModelMapper(), accounts);

    @Test
    void saveTeacherHashesPasswordAndNormalizesValues() {
        TeacherDTO input = new TeacherDTO();
        input.setName("  Dr. Ada  ");
        input.setDesignation("  Senior Lecturer ");
        input.setUsername("teacher");
        input.setEmail("  TEACHER@EXAMPLE.TEST ");
        input.setPassword("Valid-password-123");
        when(encoder.encode("Valid-password-123")).thenReturn("encoded-password");
        when(repository.saveAndFlush(any(Teacher.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TeacherDTO saved = service.saveTeacher(input);

        assertEquals("Dr. Ada", saved.getName());
        assertEquals("Senior Lecturer", saved.getDesignation());
        assertEquals("teacher", saved.getUsername());
        assertEquals("teacher@example.test", saved.getEmail());
        assertEquals(null, saved.getPassword());
        verify(encoder).encode("Valid-password-123");
    }

}
