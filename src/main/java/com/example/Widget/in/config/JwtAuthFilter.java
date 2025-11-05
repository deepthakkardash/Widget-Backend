package com.example.Widget.in.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractJwtToken(request);

        // ✅ Check if token is null, blank, or "undefined"
        if (token == null || token.isBlank() || token.equalsIgnoreCase("undefined")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Additional safety: check after generation/extraction
        if (!StringUtils.hasText(token)) {
            System.out.println("⚠️ JWT token found but is blank or invalid!");
            filterChain.doFilter(request, response);
            return;
        }

        String username = null;

        try {
            username = jwtTokenUtil.extractUsername(token);
        } catch (Exception e) {
            System.out.println("⚠️ Invalid or expired JWT token: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // Continue only if username is valid and not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // ✅ Ensure token is valid for the extracted username
            if (jwtTokenUtil.validateToken(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println("⚠️ Token validation failed for username: " + username);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract JWT token from the Authorization header or cookies.
     */
    private String extractJwtToken(HttpServletRequest request) {
        // 1️⃣ Try Authorization header
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        // 2️⃣ Try from cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("Authorization".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // 3️⃣ No token found
        return null;
    }
}
