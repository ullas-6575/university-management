package com.ullas.University.management.service;

import com.ullas.University.management.dto.*;
import com.ullas.University.management.entity.*;
import com.ullas.University.management.repository.TeacherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;
    private final AccountService accounts;

    public TeacherService(TeacherRepository repository, ModelMapper modelMapper, AccountService accounts) {
        this.teacherRepository = repository;
        this.modelMapper = modelMapper;
        this.accounts = accounts;
    }

    public Page<TeacherDTO> getAllTeachers(int page, int size) {
        if (page < 0 || page > 10000 || size < 1 || size > 100)
            throw new IllegalArgumentException("page must be 0..10000 and size 1..100");
        return teacherRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending())).map(this::toDTO);
    }

    public TeacherDTO getTeacherById(Long id) {
        return toDTO(require(id));
    }

    @Transactional
    public TeacherDTO saveTeacher(TeacherDTO dto) {
        Teacher entity = modelMapper.map(dto, Teacher.class);
        entity.setName(dto.getName().strip());
        entity.setDesignation(dto.getDesignation().strip());
        entity.setUser(accounts.create(dto, Role.TEACHER));
        return toDTO(teacherRepository.saveAndFlush(entity));
    }

    @Transactional
    public TeacherDTO updateTeacher(Long id, TeacherDTO dto) {
        Teacher entity = require(id);
        accounts.update(entity.getUser(), dto);
        entity.setName(dto.getName().strip());
        entity.setDesignation(dto.getDesignation().strip());
        return toDTO(teacherRepository.saveAndFlush(entity));
    }

    @Transactional
    public void deleteTeacher(Long id) {

        teacherRepository.delete(require(id));
        teacherRepository.flush();
    }

    private Teacher require(Long id) {
        return teacherRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    private TeacherDTO toDTO(Teacher entity) {
        TeacherDTO dto = modelMapper.map(entity, TeacherDTO.class);
        dto.setUsername(entity.getUser().getUsername());
        dto.setEmail(entity.getUser().getEmail());
        dto.setPassword(null);
        return dto;
    }
}
