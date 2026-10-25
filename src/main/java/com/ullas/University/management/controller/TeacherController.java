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
@Controller @RequestMapping("/teachers") @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
public class TeacherController {
    private final TeacherService teacherService;
    public TeacherController(TeacherService service){this.teacherService=service;}
    @InitBinder public void bind(WebDataBinder binder){
        binder.registerCustomEditor(String.class,"password",new PropertyEditorSupport(){@Override public void setAsText(String text){setValue(text.isEmpty()?null:text);}});
        binder.setDisallowedFields("id","user","role");
    }
    @GetMapping public String getTeachers(@RequestParam(defaultValue="0") int page,Model model){
        var result=teacherService.getAllTeachers(page,20);
        model.addAttribute("teachers",result.getContent());model.addAttribute("result",result);
        return "teachers";
    }
    @GetMapping("/add") public String addTeacher(Model model){
        model.addAttribute("teacher",new TeacherDTO());return "teacher-form";
    }
    @PostMapping("/store") public String storeTeacher(@Validated(ValidationGroups.Create.class) @ModelAttribute("teacher") TeacherDTO dto,
        BindingResult errors,Model model){
        if(errors.hasErrors()){dto.setPassword(null);return "teacher-form";}
        try{teacherService.saveTeacher(dto);}
        catch(DataIntegrityViolationException|ConflictException error){errors.reject("conflict","Username, email, or roll already exists");dto.setPassword(null);return "teacher-form";}
        return "redirect:/teachers";
    }
    @GetMapping("/{id}/edit") public String editTeacher(@PathVariable Long id,Model model){
        model.addAttribute("teacher",teacherService.getTeacherById(id));model.addAttribute("editing",true);
        return "teacher-form";
    }
    @PostMapping("/{id}/update") public String updateTeacher(@PathVariable Long id,
        @Validated(ValidationGroups.Update.class) @ModelAttribute("teacher") TeacherDTO dto,BindingResult errors,Model model){
        model.addAttribute("editing",true);dto.setId(id);
        if(errors.hasErrors()){dto.setPassword(null);return "teacher-form";}
        try{teacherService.updateTeacher(id,dto);}
        catch(DataIntegrityViolationException|ConflictException|IllegalArgumentException error){
            errors.reject("conflict","Username cannot change; email or roll may already exist");dto.setPassword(null);return "teacher-form";
        }
        return "redirect:/teachers";
    }
    @PostMapping("/{id}/delete") public String deleteTeacher(@PathVariable Long id,RedirectAttributes flash){
        try{teacherService.deleteTeacher(id);flash.addFlashAttribute("message","Account deleted");}
        catch(ConflictException error){flash.addFlashAttribute("message",error.getMessage());}
        return "redirect:/teachers";
    }
}
