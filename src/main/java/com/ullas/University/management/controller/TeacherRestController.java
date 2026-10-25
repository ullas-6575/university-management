package com.example.webapp.controller;
import java.net.URI;
import com.example.webapp.dto.*;
import com.example.webapp.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/teachers") @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
public class TeacherRestController {
    private final TeacherService service;
    public TeacherRestController(TeacherService service){this.service=service;}
    @GetMapping public PageResponse<TeacherDTO> list(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size){
        return PageResponse.from(service.getAllTeachers(page,size));
    }
    @GetMapping("/{id}") public TeacherDTO get(@PathVariable Long id){return service.getTeacherById(id);}
    @PostMapping public ResponseEntity<TeacherDTO> create(@Validated(ValidationGroups.Create.class) @RequestBody TeacherDTO dto){
        TeacherDTO saved=service.saveTeacher(dto);
        return ResponseEntity.created(URI.create("/api/teachers/"+saved.getId())).body(saved);
    }
    @PutMapping("/{id}") public TeacherDTO update(@PathVariable Long id,@Validated(ValidationGroups.Update.class) @RequestBody TeacherDTO dto){
        return service.updateTeacher(id,dto);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteTeacher(id);return ResponseEntity.noContent().build();
    }
}
