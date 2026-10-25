package com.example.webapp.controller;
import java.net.URI;
import com.example.webapp.dto.*;
import com.example.webapp.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/admins") @PreAuthorize("hasRole('ADMIN')")
public class AdminRestController {
    private final AdminService service;
    public AdminRestController(AdminService service){this.service=service;}
    @GetMapping public PageResponse<AdminDTO> list(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size){
        return PageResponse.from(service.getAllAdmins(page,size));
    }
    @GetMapping("/{id}") public AdminDTO get(@PathVariable Long id){return service.getAdminById(id);}
    @PostMapping public ResponseEntity<AdminDTO> create(@Validated(ValidationGroups.Create.class) @RequestBody AdminDTO dto){
        AdminDTO saved=service.saveAdmin(dto);
        return ResponseEntity.created(URI.create("/api/admins/"+saved.getId())).body(saved);
    }
    @PutMapping("/{id}") public AdminDTO update(@PathVariable Long id,@Validated(ValidationGroups.Update.class) @RequestBody AdminDTO dto){
        return service.updateAdmin(id,dto);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteAdmin(id);return ResponseEntity.noContent().build();
    }
}
