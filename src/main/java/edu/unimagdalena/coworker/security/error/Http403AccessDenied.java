package edu.unimagdalena.coworker.security.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Http403AccessDenied implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        response.setStatus(403);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            new ObjectMapper().writeValue(response.getOutputStream(),
                    Map.of("status", 403, "error", "Forbidden", "message", "Access denied"));
        } catch (Exception ignored) {}
    }
}
