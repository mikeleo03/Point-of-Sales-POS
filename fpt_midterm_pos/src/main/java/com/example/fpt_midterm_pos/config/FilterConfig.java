package com.example.fpt_midterm_pos.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.fpt_midterm_pos.config.filter.ApiKeyFilter;
import com.example.fpt_midterm_pos.config.filter.JwtRequestFilter;
import com.example.fpt_midterm_pos.data.repository.ApiKeyRepository;
import com.example.fpt_midterm_pos.service.AuthService;
import com.example.fpt_midterm_pos.utils.JwtUtils;

@Configuration
public class FilterConfig {

    private final ApiKeyRepository apiKeyRepository;
    private final AuthService authService;
    private final JwtUtils jwtUtil;

    public FilterConfig(ApiKeyRepository apiKeyRepository, AuthService authService, JwtUtils jwtUtil) {
        this.apiKeyRepository = apiKeyRepository;
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @Bean(name = "apiKeyFilterBean")
    public ApiKeyFilter apiKeyFilter() {
        return new ApiKeyFilter(apiKeyRepository);
    }

    @Bean(name = "apiKeyFilterRegistrationBean")
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistration(ApiKeyFilter apiKeyFilter) {
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiKeyFilter);
        registrationBean.addUrlPatterns("/api/*"); // All API
        return registrationBean;
    }

    @Bean(name = "jwtRequestFilterBean")
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(authService, jwtUtil);
    }

    @Bean(name = "jwtRequestFilterRegistrationBean")
    public FilterRegistrationBean<JwtRequestFilter> jwtRequestFilterRegistration(JwtRequestFilter jwtRequestFilter) {
        FilterRegistrationBean<JwtRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtRequestFilter);
        registrationBean.addUrlPatterns("/api/*"); // All API except /api/v1/authentication is handled in the filter
        return registrationBean;
    }
}
