package com.umograd.analytic.dto.report;

import java.util.List;
import java.util.Map;

public record ChildReportDto(
        List<ProgressPoint> progressPoints,
        List<TimePoint> timePoints,
        Map<String, Long> difficultyStats
) {
    public record ProgressPoint(String date, int score) {}
    public record TimePoint(String date, long averageSeconds) {}
}