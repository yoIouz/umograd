package com.umograd.content.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthenticationToken extends AbstractAuthenticationToken {
    private final String principal;
    private final TokenDecoder.Claims claims;

    public UserAuthenticationToken(String principal,
                                   Collection<? extends GrantedAuthority> authorities,
                                   TokenDecoder.Claims claims) {
        super(authorities);
        this.principal = principal;
        this.claims = claims;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public TokenDecoder.Claims getClaims() {
        return claims;
    }
}
