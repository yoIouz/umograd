package com.umograd.analytic.dto.achievement;

import java.time.LocalDateTime;

public record TaskResultSpelDto(
        String status,
        Integer score,
        String difficulty,
        String taskType,
        LocalDateTime finishedAt,
        int maxCorrectStreak,
        int maxQuizStreak,
        int totalScoreToday
) {}
