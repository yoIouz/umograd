package com.umograd.domain.user;

public record SessionStatusDto(
        String status,
        String message,
        long minutesLeft
) {}