package com.umograd.presentation.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String error;

    public AuthResponse(String error) {
        this(null, null, null, error);
    }

    public AuthResponse(String accessToken, String refreshToken, Long userId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
}
