package com.rt.engine.common.component;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;

public class LogbackFilter implements Filter {

    private static final String TRACE_ID = "traceId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        boolean result = this.insertMdc();
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            if (result) {
                MDC.remove(TRACE_ID);
            }
        }
    }

    private boolean insertMdc() {
        try {
            MDC.put(TRACE_ID, UUID.randomUUID().toString().replace("-", ""));
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
