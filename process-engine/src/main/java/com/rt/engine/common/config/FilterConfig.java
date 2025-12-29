package com.rt.engine.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.rt.engine.common.component.LogbackFilter;
import com.rt.engine.common.filter.AuthenticationFilter;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<LogbackFilter> getLogbackFilter() {
        FilterRegistrationBean<LogbackFilter> logbackFilter = new FilterRegistrationBean<>(new LogbackFilter());
        logbackFilter.addUrlPatterns("/*");
        logbackFilter.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return logbackFilter;
    }

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilterRegistration() {
        FilterRegistrationBean<AuthenticationFilter> registration = new FilterRegistrationBean();
        registration.setFilter(new AuthenticationFilter());
        registration.addUrlPatterns("/*");
        registration.setName("authenticationFilter");
        registration.setOrder(1);
        return registration;
    }
}
