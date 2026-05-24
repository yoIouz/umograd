package com.umograd.analytic.dto.achievement;

public record AchievementGrantResponse(
        String name,
        String description,
        String iconUrl,
        boolean newlyEarned
) {}

