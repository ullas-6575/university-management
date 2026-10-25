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
@Controller @RequestMapping("/admins") @PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService service){this.adminService=service;}
    @InitBinder public void bind(WebDataBinder binder){
        binder.registerCustomEditor(String.class,"password",new PropertyEditorSupport(){@Override public void setAsText(String text){setValue(text.isEmpty()?null:text);}});
        binder.setDisallowedFields("id","user","role");
    }
    @GetMapping public String getAdmins(@RequestParam(defaultValue="0") int page,Model model){
        var result=adminService.getAllAdmins(page,20);
        model.addAttribute("admins",result.getContent());model.addAttribute("result",result);
        return "admins";
    }
    @GetMapping("/add") public String addAdmin(Model model){
        model.addAttribute("admin",new AdminDTO());return "admin-form";
    }
    @PostMapping("/store") public String storeAdmin(@Validated(ValidationGroups.Create.class) @ModelAttribute("admin") AdminDTO dto,
        BindingResult errors,Model model){
        if(errors.hasErrors()){dto.setPassword(null);return "admin-form";}
        try{adminService.saveAdmin(dto);}
        catch(DataIntegrityViolationException|ConflictException error){errors.reject("conflict","Username, email, or roll already exists");dto.setPassword(null);return "admin-form";}
        return "redirect:/admins";
    }
    @GetMapping("/{id}/edit") public String editAdmin(@PathVariable Long id,Model model){
        model.addAttribute("admin",adminService.getAdminById(id));model.addAttribute("editing",true);
        return "admin-form";
    }
    @PostMapping("/{id}/update") public String updateAdmin(@PathVariable Long id,
        @Validated(ValidationGroups.Update.class) @ModelAttribute("admin") AdminDTO dto,BindingResult errors,Model model){
        model.addAttribute("editing",true);dto.setId(id);
        if(errors.hasErrors()){dto.setPassword(null);return "admin-form";}
        try{adminService.updateAdmin(id,dto);}
        catch(DataIntegrityViolationException|ConflictException|IllegalArgumentException error){
            errors.reject("conflict","Username cannot change; email or roll may already exist");dto.setPassword(null);return "admin-form";
        }
        return "redirect:/admins";
    }
    @PostMapping("/{id}/delete") public String deleteAdmin(@PathVariable Long id,RedirectAttributes flash){
        try{adminService.deleteAdmin(id);flash.addFlashAttribute("message","Account deleted");}
        catch(ConflictException error){flash.addFlashAttribute("message",error.getMessage());}
        return "redirect:/admins";
    }
}
