package com.ullas.University.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ullas.University.management.dto.StudentDTO;
import com.ullas.University.management.entity.Student;
import com.ullas.University.management.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

class StudentServiceTest {
    private final StudentRepository repository = mock(StudentRepository.class);
    private final PasswordEncoder encoder = mock(PasswordEncoder.class);
    private final AccountService accounts = new AccountService(encoder);
    private final StudentService service = new StudentService(repository, new ModelMapper(), accounts);

    @Test
    void saveStudentHashesPasswordAndNormalizesValues() {
        StudentDTO input = new StudentDTO();
        input.setName("  Alice Student  ");
        input.setRoll("  CSE-101 ");
        input.setUsername("student");
        input.setEmail("  STUDENT@EXAMPLE.TEST ");
        input.setPassword("Valid-password-123");
        when(encoder.encode("Valid-password-123")).thenReturn("encoded-password");
        when(repository.saveAndFlush(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudentDTO saved = service.saveStudent(input);

        assertEquals("Alice Student", saved.getName());
        assertEquals("CSE-101", saved.getRoll());
        assertEquals("student", saved.getUsername());
        assertEquals("student@example.test", saved.getEmail());
        assertEquals(null, saved.getPassword());
        verify(encoder).encode("Valid-password-123");
    }
}
