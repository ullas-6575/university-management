package com.example.webapp.controller;
import java.util.Map;
import com.example.webapp.config.CampusPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth")
public class AuthController {
    public record CsrfResponse(String headerName,String parameterName,String token){}
    @GetMapping("/csrf") public CsrfResponse csrf(CsrfToken token){return new CsrfResponse(token.getHeaderName(),token.getParameterName(),token.getToken());}
    @GetMapping("/me") public Map<String,Object> me(@AuthenticationPrincipal CampusPrincipal principal){
        return Map.of("id",principal.id(),"username",principal.getUsername(),"role",principal.role());
    }
}
