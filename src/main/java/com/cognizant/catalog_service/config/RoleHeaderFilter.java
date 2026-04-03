package com.cognizant.catalog_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RoleHeaderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        String role = request.getHeader("X-USER-ROLE");
        if (role != null) {
            // Ensure the role starts with ROLE_ for hasRole() to work
            String formattedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(formattedRole);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    request.getHeader("X-USER-ID"), null, Collections.singletonList(authority));

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}