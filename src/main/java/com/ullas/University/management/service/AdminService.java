package com.ullas.University.management.service;

import com.ullas.University.management.dto.*;
import com.ullas.University.management.entity.*;
import com.ullas.University.management.repository.AdminRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminService {
    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;
    private final AccountService accounts;

    public AdminService(AdminRepository repository, ModelMapper modelMapper, AccountService accounts) {
        this.adminRepository = repository;
        this.modelMapper = modelMapper;
        this.accounts = accounts;
    }

    public Page<AdminDTO> getAllAdmins(int page, int size) {
        if (page < 0 || page > 10000 || size < 1 || size > 100)
            throw new IllegalArgumentException("page must be 0..10000 and size 1..100");
        return adminRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending())).map(this::toDTO);
    }

    public AdminDTO getAdminById(Long id) {
        return toDTO(require(id));
    }

    @Transactional
    public AdminDTO saveAdmin(AdminDTO dto) {
        Admin entity = modelMapper.map(dto, Admin.class);
        entity.setName(dto.getName().strip());
        entity.setOffice(dto.getOffice().strip());
        entity.setUser(accounts.create(dto, Role.ADMIN));
        return toDTO(adminRepository.saveAndFlush(entity));
    }

    @Transactional
    public AdminDTO updateAdmin(Long id, AdminDTO dto) {
        Admin entity = require(id);
        accounts.update(entity.getUser(), dto);
        entity.setName(dto.getName().strip());
        entity.setOffice(dto.getOffice().strip());
        return toDTO(adminRepository.saveAndFlush(entity));
    }

    @Transactional
    public void deleteAdmin(Long id) {
        var admins = adminRepository.lockAll();
        if (admins.stream().noneMatch(a -> a.getId().equals(id)))
            throw new NotFoundException();
        if (admins.size() == 1)
            throw new ConflictException("The last admin cannot be deleted");
        adminRepository.delete(require(id));
        adminRepository.flush();
    }

    private Admin require(Long id) {
        return adminRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    private AdminDTO toDTO(Admin entity) {
        AdminDTO dto = modelMapper.map(entity, AdminDTO.class);
        dto.setUsername(entity.getUser().getUsername());
        dto.setEmail(entity.getUser().getEmail());
        dto.setPassword(null);
        return dto;
    }
}
