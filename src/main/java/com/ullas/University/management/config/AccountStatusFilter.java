package com.ullas.University.management.config;

import java.io.IOException;
import com.ullas.University.management.repository.AppUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

// Constructed only in SecurityConfig, so it is not registered twice as a servlet filter.
public class AccountStatusFilter extends OncePerRequestFilter {
    private final AppUserRepository users;

    public AccountStatusFilter(AppUserRepository users) {
        this.users = users;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CampusPrincipal principal) {
            var current = users.findById(principal.id());
            if (current.isEmpty() || current.get().getAuthVersion() != principal.authVersion()
                    || !current.get().getRole().name().equals(principal.role())) {
                var session = request.getSession(false);
                if (session != null)
                    session.invalidate();
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
