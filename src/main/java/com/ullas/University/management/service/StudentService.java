package com.ullas.University.management.service;

import com.ullas.University.management.dto.*;
import com.ullas.University.management.entity.*;
import com.ullas.University.management.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final AccountService accounts;

    public StudentService(StudentRepository repository, ModelMapper modelMapper, AccountService accounts) {
        this.studentRepository = repository;
        this.modelMapper = modelMapper;
        this.accounts = accounts;
    }

    public Page<StudentDTO> getAllStudents(int page, int size) {
        if (page < 0 || page > 10000 || size < 1 || size > 100)
            throw new IllegalArgumentException("page must be 0..10000 and size 1..100");
        return studentRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending())).map(this::toDTO);
    }

    public StudentDTO getStudentById(Long id) {
        return toDTO(require(id));
    }

    @Transactional
    public StudentDTO saveStudent(StudentDTO dto) {
        Student entity = modelMapper.map(dto, Student.class);
        entity.setName(dto.getName().strip());
        entity.setRoll(dto.getRoll().strip());
        entity.setUser(accounts.create(dto, Role.STUDENT));
        return toDTO(studentRepository.saveAndFlush(entity));
    }

    @Transactional
    public StudentDTO updateStudent(Long id, StudentDTO dto) {
        Student entity = require(id);
        accounts.update(entity.getUser(), dto);
        entity.setName(dto.getName().strip());
        entity.setRoll(dto.getRoll().strip());
        return toDTO(studentRepository.saveAndFlush(entity));
    }

    @Transactional
    public void deleteStudent(Long id) {

        studentRepository.delete(require(id));
        studentRepository.flush();
    }

    private Student require(Long id) {
        return studentRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    private StudentDTO toDTO(Student entity) {
        StudentDTO dto = modelMapper.map(entity, StudentDTO.class);
        dto.setUsername(entity.getUser().getUsername());
        dto.setEmail(entity.getUser().getEmail());
        dto.setPassword(null);
        return dto;
    }
}
