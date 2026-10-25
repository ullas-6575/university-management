package com.example.webapp.controller;
import com.example.webapp.dto.*;
import com.example.webapp.service.*;
import java.beans.PropertyEditorSupport;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller @RequestMapping("/students") @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
public class StudentController {
    private final StudentService studentService;
    public StudentController(StudentService service){this.studentService=service;}
    @InitBinder public void bind(WebDataBinder binder){
        binder.registerCustomEditor(String.class,"password",new PropertyEditorSupport(){@Override public void setAsText(String text){setValue(text.isEmpty()?null:text);}});
        binder.setDisallowedFields("id","user","role");
    }
    @GetMapping public String getStudents(@RequestParam(defaultValue="0") int page,Model model){
        var result=studentService.getAllStudents(page,20);
        model.addAttribute("students",result.getContent());model.addAttribute("result",result);
        return "students";
    }
    @GetMapping("/add") public String addStudent(Model model){
        model.addAttribute("student",new StudentDTO());return "student-form";
    }
    @PostMapping("/store") public String storeStudent(@Validated(ValidationGroups.Create.class) @ModelAttribute("student") StudentDTO dto,
        BindingResult errors,Model model){
        if(errors.hasErrors()){dto.setPassword(null);return "student-form";}
        try{studentService.saveStudent(dto);}
        catch(DataIntegrityViolationException|ConflictException error){errors.reject("conflict","Username, email, or roll already exists");dto.setPassword(null);return "student-form";}
        return "redirect:/students";
    }
    @GetMapping("/{id}/edit") public String editStudent(@PathVariable Long id,Model model){
        model.addAttribute("student",studentService.getStudentById(id));model.addAttribute("editing",true);
        return "student-form";
    }
    @PostMapping("/{id}/update") public String updateStudent(@PathVariable Long id,
        @Validated(ValidationGroups.Update.class) @ModelAttribute("student") StudentDTO dto,BindingResult errors,Model model){
        model.addAttribute("editing",true);dto.setId(id);
        if(errors.hasErrors()){dto.setPassword(null);return "student-form";}
        try{studentService.updateStudent(id,dto);}
        catch(DataIntegrityViolationException|ConflictException|IllegalArgumentException error){
            errors.reject("conflict","Username cannot change; email or roll may already exist");dto.setPassword(null);return "student-form";
        }
        return "redirect:/students";
    }
    @PostMapping("/{id}/delete") public String deleteStudent(@PathVariable Long id,RedirectAttributes flash){
        try{studentService.deleteStudent(id);flash.addFlashAttribute("message","Account deleted");}
        catch(ConflictException error){flash.addFlashAttribute("message",error.getMessage());}
        return "redirect:/students";
    }
}
