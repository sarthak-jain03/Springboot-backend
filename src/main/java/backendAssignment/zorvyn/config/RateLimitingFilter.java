package backendAssignment.zorvyn.config;

import backendAssignment.zorvyn.dto.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    @Value("${ratelimiting.requestperminute:60}")
    private int maxRequests;

    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private long lastResetTime = System.currentTimeMillis();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        long now = System.currentTimeMillis();
        if (now-lastResetTime > 60_000) {
            requestCounts.clear();
            lastResetTime = now;
        }
        String ip = getClientIp(request);
        int count = requestCounts.getOrDefault(ip, 0);

        if (count < maxRequests) {
            requestCounts.put(ip, count + 1);
            filterChain.doFilter(request, response); // allow
        }
        else{
            sendRateLimitError(response, request);
        }
    }

    private void sendRateLimitError(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setStatus(429);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .timeStamp(LocalDateTime.now())
                .status(429)
                .error("Too Many Requests")
                .message("Rate limit exceeded")
                .path(request.getRequestURI())
                .build();

        new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValue(response.getOutputStream(), error);
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return (forwarded != null && !forwarded.isEmpty())
                ? forwarded.split(",")[0]
                : request.getRemoteAddr();
    }
}