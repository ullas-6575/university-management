package com.example.webapp.controller;
import java.net.URI;
import com.example.webapp.dto.*;
import com.example.webapp.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/students") @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
public class StudentRestController {
    private final StudentService service;
    public StudentRestController(StudentService service){this.service=service;}
    @GetMapping public PageResponse<StudentDTO> list(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size){
        return PageResponse.from(service.getAllStudents(page,size));
    }
    @GetMapping("/{id}") public StudentDTO get(@PathVariable Long id){return service.getStudentById(id);}
    @PostMapping public ResponseEntity<StudentDTO> create(@Validated(ValidationGroups.Create.class) @RequestBody StudentDTO dto){
        StudentDTO saved=service.saveStudent(dto);
        return ResponseEntity.created(URI.create("/api/students/"+saved.getId())).body(saved);
    }
    @PutMapping("/{id}") public StudentDTO update(@PathVariable Long id,@Validated(ValidationGroups.Update.class) @RequestBody StudentDTO dto){
        return service.updateStudent(id,dto);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteStudent(id);return ResponseEntity.noContent().build();
    }
}
