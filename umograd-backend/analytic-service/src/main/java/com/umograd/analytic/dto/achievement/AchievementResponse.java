package com.umograd.analytic.dto.achievement;

public record AchievementResponse(
        Long id,
        String name,
        String description,
        String iconUrl,
        Integer conditionValue
) {
}
