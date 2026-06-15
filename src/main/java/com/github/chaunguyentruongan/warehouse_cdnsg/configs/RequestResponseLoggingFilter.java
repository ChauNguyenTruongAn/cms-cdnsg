package com.github.chaunguyentruongan.warehouse_cdnsg.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    // Danh sách các đường dẫn cần bỏ qua log để tránh gây nhiễu log (như swagger, assets tĩnh)
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/swagger-ui",
            "/v3/api-docs",
            "/favicon.ico"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        
        // Kiểm tra xem request URI có khớp với đường dẫn loại trừ hay không
        boolean isExcluded = EXCLUDED_PATHS.stream().anyMatch(uri::startsWith);

        if (isExcluded) {
            filterChain.doFilter(request, response);
            return;
        }

        long startTime = System.currentTimeMillis();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        String clientIp = request.getRemoteAddr();
        String fullUri = uri + (queryString != null ? "?" + queryString : "");

        log.info(">>> Incoming Request: [{}] {} | Client IP: {}", method, fullUri, clientIp);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();
            log.info("<<< Outgoing Response: [{}] {} | Status: {} | Duration: {}ms", method, fullUri, status, duration);
        }
    }
}
