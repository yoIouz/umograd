package com.umograd.content.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT фильтр: ожидает Authorization: Bearer <token>.
 * Декодирует токен через TokenDecoder и кладёт Authentication в SecurityContext.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenDecoder tokenDecoder;

    public JwtAuthenticationFilter(TokenDecoder tokenDecoder) {
        this.tokenDecoder = tokenDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                var claims = tokenDecoder.decode(token);
                String username = claims.username();
                List<String> roles = claims.roles();

                System.out.println("JWT decoded for user: " + username);
                System.out.println("Roles from token: " + roles);

                var authorities = roles.stream()
                        .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                System.out.println("Authorities set in context: " + authorities);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        claims, null, claims.roles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                System.out.println("JWT decode failed: " + e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
