package com.umograd.analytic.dto;

import lombok.Data;

@Data
public class TaskAnalyticsResponse {
    private Long taskId;
    private String title;
    private Double averageScore;
    private Long totalAttempts;
    private String difficulty;
    private String recommendation;
}

