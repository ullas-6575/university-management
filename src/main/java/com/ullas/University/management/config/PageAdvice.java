package com.ullas.University.management.config;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class PageAdvice {
    @ModelAttribute
    public void identity(Authentication authentication, Model model) {
        if (authentication != null) {
            model.addAttribute("currentUsername", authentication.getName());
            model.addAttribute("currentRole", authentication.getAuthorities().stream()
                    .map(a -> a.getAuthority().replace("ROLE_", "")).findFirst().orElse(""));
        }
    }
}
