package com.example.fpt_midterm_pos.config.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.fpt_midterm_pos.data.model.User;
import com.example.fpt_midterm_pos.service.AuthService;
import com.example.fpt_midterm_pos.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final JwtUtils jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    public JwtRequestFilter(AuthService authService, JwtUtils jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        logger.info("[JWTFilter][" + request + "]" + "[" + request.getMethod() + "] " + requestURI);

        // Exclude the /api/v1/authentication endpoint and any nested routes
        if (requestURI.startsWith("/api/v1/authentication")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getMethod().equals("GET") ||
                request.getMethod().equals("POST") ||
                request.getMethod().equals("PUT") ||
                request.getMethod().equals("DELETE") ||
                request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT and validate it
        final String authorizationHeader = request.getHeader("Authorization");

        // Initialize the values
        String username;
        String jwt;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Authorization header is not defined\"}");
            return;
        }

        if (username != null) {
            User userDetails = authService.findByUsername(username);

            if (userDetails != null) {
                if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
                    // Only if JWT token is valid
                    logger.info("[JWTFilter] doFilter starts.");
                    filterChain.doFilter(request, response);
                    logger.info("[JWTFilter] doFilter done.");
                }
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"User not found\"}");
                return;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"JWT token is invalid\"}");
            return;
        }

        logger.info("[JWTFilter] Logging Response : {}", response.getStatus());
    }
}
