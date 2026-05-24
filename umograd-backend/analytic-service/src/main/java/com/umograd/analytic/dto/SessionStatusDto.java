package com.umograd.analytic.dto;

public record SessionStatusDto(
        String status,
        String message,
        long minutesLeft
) {}