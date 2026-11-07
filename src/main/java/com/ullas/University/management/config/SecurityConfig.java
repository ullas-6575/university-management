package com.ullas.University.management.config;

import java.util.Map;
import com.ullas.University.management.repository.AppUserRepository;
import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static boolean json(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/")
                || request.getHeader("Accept") != null && request.getHeader("Accept").contains("application/json");
    }

    @Bean
    SecurityFilterChain security(HttpSecurity http, ObjectMapper mapper, AppUserRepository users) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/favicon.ico", "/error", "/api/auth/csrf").permitAll()
                .requestMatchers("/", "/api/auth/me").authenticated()
                .requestMatchers("/admins", "/admins/**", "/api/admins", "/api/admins/**").hasRole("ADMIN")
                .requestMatchers("/teachers", "/teachers/**", "/api/teachers", "/api/teachers/**")
                .hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers("/students", "/students/**", "/api/students", "/api/students/**")
                .hasAnyRole("ADMIN", "STUDENT")
                .anyRequest().denyAll())
                .formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            if (json(request)) {
                                response.setStatus(200);
                                response.setContentType("application/json");
                                var principal = (CampusPrincipal) authentication.getPrincipal();
                                mapper.writeValue(response.getWriter(),
                                        Map.of("username", principal.getUsername(), "role", principal.role()));
                            } else
                                response.sendRedirect("/");
                        }).failureHandler((request, response, error) -> {
                            if (json(request)) {
                                response.setStatus(401);
                                response.setContentType("application/problem+json");
                                mapper.writeValue(response.getWriter(), Map.of("status", 401, "title", "Unauthorized",
                                        "detail", "Invalid username or password"));
                            } else
                                response.sendRedirect("/login?error");
                        }).permitAll())
                .logout(logout -> logout.logoutUrl("/logout").deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            if (json(request))
                                response.setStatus(204);
                            else
                                response.sendRedirect("/login?logout");
                        }).permitAll())
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, error) -> {
                            if (json(request)) {
                                response.setStatus(401);
                                response.setContentType("application/problem+json");
                                mapper.writeValue(response.getWriter(),
                                        Map.of("status", 401, "title", "Unauthorized", "detail", "Login required"));
                            } else
                                response.sendRedirect("/login");
                        }).accessDeniedHandler((request, response, error) -> {
                            if (json(request)) {
                                response.setStatus(403);
                                response.setContentType("application/problem+json");
                                mapper.writeValue(response.getWriter(), Map.of("status", 403, "title", "Forbidden",
                                        "detail", "Permission denied or invalid CSRF token"));
                            } else
                                response.sendError(403);
                        }))
                .addFilterBefore(new AccountStatusFilter(users), AnonymousAuthenticationFilter.class);
        return http.build(); // CSRF remains enabled for session authentication.
    }
}
