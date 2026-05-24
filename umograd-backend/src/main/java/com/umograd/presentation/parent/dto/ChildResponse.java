package com.umograd.presentation.parent.dto;

public record ChildResponse(
        Long id,
        String username,
        String email,
        boolean parentConsent
) {}
