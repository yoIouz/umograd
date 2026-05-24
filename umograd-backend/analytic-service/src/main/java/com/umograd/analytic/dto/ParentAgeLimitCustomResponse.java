package com.umograd.analytic.dto;

public record ParentAgeLimitCustomResponse(
        long childId,
        long parentId,
        int age,
        int customMinutes
) {
}
