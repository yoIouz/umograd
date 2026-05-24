package com.umograd.analytic.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

@Configuration
public class JwtTokenDecoderConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public TokenDecoder tokenDecoder() {
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return token -> {
            Claims body = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = body.get("username", String.class);
            @SuppressWarnings("unchecked")
            List<String> roles = body.get("roles", List.class);
            Long childId = body.get("childId", Long.class);
            Long userId = Long.valueOf(body.getSubject());
            String email = body.get("email", String.class);

            return new TokenDecoder.Claims(username, roles, childId, userId, email);
        };
    }
}
