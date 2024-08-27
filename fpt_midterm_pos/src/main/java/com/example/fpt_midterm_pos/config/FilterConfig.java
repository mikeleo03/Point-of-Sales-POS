package com.example.fpt_midterm_pos.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.fpt_midterm_pos.config.filter.ApiKeyFilter;

@Configuration
public class FilterConfig {

    @Bean(name = "apiKeyFilterBean")
    public ApiKeyFilter apiKeyFilter() {
        return new ApiKeyFilter();
    }

    @Bean(name = "apiKeyFilterRegistrationBean")
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistration(ApiKeyFilter apiKeyFilter) {
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiKeyFilter);
        registrationBean.addUrlPatterns("/api/*"); // All Customer API
        return registrationBean;
    }
}
