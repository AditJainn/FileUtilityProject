package com.example.fileconverter.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestLoggingConfig extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingConfig.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();
        String realIp = extractRealIp(request);
        String proxyIp = request.getRemoteAddr(); // Nginx IP
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String thread = Thread.currentThread().getName(); // thread name


        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            int status = response.getStatus();

            log.info("Request: thread={}, realIp={}, proxyIp={}, method={}, uri={}, status={}, duration={}ms",
                    thread, realIp, proxyIp, method, uri, status, duration);
        }
    }

    private String extractRealIp(HttpServletRequest request) {
        // 1. Cloudflare header
        String cfIp = request.getHeader("CF-Connecting-IP");
        if (cfIp != null && !cfIp.isEmpty()) {
            return cfIp;
        }

        // 2. X-Forwarded-For (first IP in the list)
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isEmpty()) {
            return xff.split(",")[0].trim();
        }

        // 3. X-Real-IP (Nginx)
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        // 4. Fallback
        return request.getRemoteAddr();
    }
}
