package com.umograd.content.security;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.Optional;

@SuppressWarnings("unused")
public interface AuthenticationHolder {

    @NonNull
    static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
            return new AnonymousAuthenticationToken("ANONYMOUS", null, new HashSet<>());
        }
        return authentication;
    }

    private static Optional<TokenDecoder.Claims> getClaims() {
        if (getAuthentication().getPrincipal() instanceof TokenDecoder.Claims claims) {
            return Optional.of(claims);
        }
        return Optional.empty();
    }

    @NonNull
    static String getUsername() {
        return getClaims().map(TokenDecoder.Claims::username).orElse("ANONYMOUS");
    }

    @NonNull
    static Long getUserId() {
        return getClaims().map(TokenDecoder.Claims::userId).orElse(0L);
    }
}
