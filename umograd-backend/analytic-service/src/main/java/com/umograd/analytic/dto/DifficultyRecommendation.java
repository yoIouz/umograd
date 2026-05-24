package com.umograd.analytic.dto;

import java.util.List;

public record DifficultyRecommendation(
        String recommendedDifficulty,
        String message,
        List<Long> parentTaskIds,
        boolean isParentDiff
) {

    public DifficultyRecommendation(String difficulty, String text, List<Long> parentTaskIds) {
        this(difficulty, text, parentTaskIds, false);
    }
}

