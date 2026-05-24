package com.umograd.analytic.service;

import com.umograd.analytic.dto.DifficultyRecommendation;
import com.umograd.analytic.dto.TaskAnalyticsResponse;

import java.util.List;

public interface AnalyticService {

    List<TaskAnalyticsResponse> getTaskStats();

    DifficultyRecommendation getRecommendation(Long childId);

    void recommendMultiple(Long childId, List<Long> taskIds);

    void deleteRecommendation(Long childId);

    List<Long> getActiveTasksId(Long childId);

    String getSelectedDiff(Long childId);

    void saveSelectedDiff(Long childId, String diff);
}
