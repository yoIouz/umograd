package com.umograd.analytic.dto;

public record ParentAgeLimitResponse(
        long childId,
        int age,
        int maxMinutes
) {
}
