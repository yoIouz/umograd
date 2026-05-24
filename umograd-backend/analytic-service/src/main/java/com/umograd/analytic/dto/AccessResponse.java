package com.umograd.analytic.dto;

public record AccessResponse(
        boolean granted,
        String message
) {}
