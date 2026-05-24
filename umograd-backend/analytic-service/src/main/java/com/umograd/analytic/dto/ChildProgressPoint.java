package com.umograd.analytic.dto;

public record ChildProgressPoint (
        String date,
        double averageScore,
        double averageTimeSeconds,
        String difficulty
) {
}
