package com.umograd.analytic.controller;

import com.umograd.analytic.dto.*;
import com.umograd.analytic.service.AnalyticService;
import com.umograd.analytic.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytics")
public class AnalyticController {

    private final AnalyticService analyticsService;

    private final ReportService reportService;

    @GetMapping("/tasks")
    public List<TaskAnalyticsResponse> getTaskStats() {
        return analyticsService.getTaskStats();
    }

    @GetMapping("/report/{childId}")
    public List<ChildProgressPoint> getReport(@PathVariable Long childId,
                                              @RequestParam(defaultValue = "month") String period) {
        return reportService.getChildReport(childId, period);
    }

    @GetMapping("/recommendation/{childId}")
    public DifficultyRecommendation getRecommendation(@PathVariable Long childId) {
        return analyticsService.getRecommendation(childId);
    }

    @PostMapping("/recommend-multiple")
    public void recommendTask(@RequestParam Long childId, @RequestBody List<Long> taskIds) {
        analyticsService.recommendMultiple(childId, taskIds);
    }

    @DeleteMapping("/recommend")
    public void removeRecommendation(@RequestParam Long childId) {
        analyticsService.deleteRecommendation(childId);
    }

    @GetMapping("/active-recs/{childId}")
    public List<Long> getActiveRecommendations(@PathVariable Long childId) {
        return analyticsService.getActiveTasksId(childId);
    }

    @PostMapping("/report/aggregate")
    public Map<Long, List<ChildProgressPoint>> getAggregateReport(@RequestBody List<Long> childIds) {
        return childIds.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> reportService.getChildReport(id, "month")
                ));
    }

    @GetMapping("/parent/selected-difficulty/{childId}")
    public String getSelectedDifficulty(@PathVariable Long childId) {
        return analyticsService.getSelectedDiff(childId);
    }

    @PostMapping("/parent/selected-difficulty")
    public void saveSelectedDifficulty(@RequestParam Long childId, @RequestParam String difficulty) {
        analyticsService.saveSelectedDiff(childId, difficulty);
    }
}
