package com.oneself.logging.core;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.oneself.logging.autoconfigure.OneselfLoggingProperties;

/**
 * 访问日志过滤器。
 */
public class AccessLogFilter extends OncePerRequestFilter {

    private final OneselfLoggingProperties properties;

    public AccessLogFilter(OneselfLoggingProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, jakarta.servlet.ServletException {
        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long costMs = System.currentTimeMillis() - start;
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String query = request.getQueryString();
            String clientIp = resolveClientIp(request);
            int status = response.getStatus();
            String fullUri = query == null ? uri : uri + "?" + query;
            Logger logger = LoggerFactory.getLogger(properties.getAccessLoggerName());
            String message = "ip=" + clientIp
                    + " method=" + method
                    + " uri=" + fullUri
                    + " status=" + status
                    + " costMs=" + costMs;
            if (costMs >= properties.getAccessSlowThresholdMs()) {
                logger.warn(message);
            } else {
                logger.info(message);
            }
        }
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            String[] parts = forwarded.split(",");
            if (parts.length > 0) {
                String candidate = parts[0].trim();
                if (!candidate.isEmpty()) {
                    return candidate;
                }
            }
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }
}
