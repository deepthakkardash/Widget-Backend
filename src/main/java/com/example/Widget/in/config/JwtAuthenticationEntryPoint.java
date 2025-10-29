package com.example.Widget.in.config;

import com.example.Widget.in.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JWT Authentication Entry Point for handling authentication failures.
 * Provides consistent ApiResponse format for JWT authentication errors.
 *
 * @since 1.0
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        logger.error("Unauthorized error: {}", authException.getMessage());

        // Log additional security information
        logSecurityEvent(request, authException);

        // Set response headers
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Create error response using ApiResponse format
        ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                .success(false)
                .message(getErrorMessage(authException))
                .build();

        // Write error response
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

    /**
     * Gets a user-friendly error message based on the specific authentication exception.
     *
     * @param authException the authentication exception
     * @return user-friendly error message
     */
    private String getErrorMessage(AuthenticationException authException) {

        // Handle Spring Security exceptions
        if (authException instanceof BadCredentialsException) {
            return authException.getMessage(); // Use specific message from JwtTokenUtil
        }

        if (authException instanceof UsernameNotFoundException) {
            System.out.println("flow is here");
            return authException.getMessage();
        }

        // Default message for unknown exceptions
        return "Authentication failed - please provide valid credentials";
    }

    /**
     * Logs security events for monitoring and debugging.
     *
     * @param request the HTTP request
     * @param authException the authentication exception
     */
    private void logSecurityEvent(HttpServletRequest request, AuthenticationException authException) {
        logger.warn("Security event - Unauthorized access attempt: {} {} from {} - Exception: {} - Message: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                authException.getClass().getSimpleName(),
                authException.getMessage());
    }
}