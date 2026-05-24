package com.umograd.analytic.unit;

import com.umograd.analytic.dto.DifficultyRecommendation;
import com.umograd.analytic.entity.task.TaskJpaEntity;
import com.umograd.analytic.entity.task.TaskResultEntity;
import com.umograd.analytic.enums.Difficulty;
import com.umograd.analytic.mapper.AnalyticsMapper;
import com.umograd.analytic.repository.task.TaskRepository;
import com.umograd.analytic.repository.task.TaskResultRepository;
import com.umograd.analytic.service.impl.DefaultAnalyticService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskRecommendationTest {

    @Mock
    private TaskResultRepository taskResultRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private AnalyticsMapper mapper;

    @InjectMocks
    private DefaultAnalyticService service;

    private final Long childId = 1L;
    private final Long taskId = 100L;

    private TaskResultEntity createMockResult(LocalDateTime start, LocalDateTime finish, Integer score, Long tId) {
        TaskResultEntity res = mock(TaskResultEntity.class);
        when(res.getStartedAt()).thenReturn(start);
        when(res.getFinishedAt()).thenReturn(finish);
        when(res.getScore()).thenReturn(score);
        when(res.getTaskId()).thenReturn(tId);
        return res;
    }

    private TaskJpaEntity createRealTask(Difficulty difficulty) {
        TaskJpaEntity task = new TaskJpaEntity();
        task.setDifficulty(difficulty);
        return task;
    }

    @Test
    void shouldReturnEasyWhenNoResultsExist() {
        when(taskResultRepository.findLastFinishedResults(childId)).thenReturn(Collections.emptyList());

        DifficultyRecommendation recommendation = service.getRecommendation(childId);

        assertEquals("EASY", recommendation.recommendedDifficulty());
        assertTrue(recommendation.message().contains("Начните с простого"));
        verifyNoInteractions(taskRepository);
    }

    @Test
    void shouldUpgradeFromEasyToMediumOnHighPerformance() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime finish = start.plusSeconds(20);
        TaskResultEntity resultEntity = createMockResult(start, finish, 100, taskId);

        when(taskResultRepository.findLastFinishedResults(childId)).thenReturn(List.of(resultEntity));
        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(createRealTask(Difficulty.EASY)));

        DifficultyRecommendation recommendation = service.getRecommendation(childId);

        assertEquals("MEDIUM", recommendation.recommendedDifficulty());
        assertTrue(recommendation.message().contains("Уровень сложности повышен"));
    }

    @Test
    void shouldUpgradeFromMediumToHardOnHighPerformance() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime finish = start.plusSeconds(10);
        TaskResultEntity resultEntity = createMockResult(start, finish, 105, taskId);

        when(taskResultRepository.findLastFinishedResults(childId)).thenReturn(List.of(resultEntity));
        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(createRealTask(Difficulty.MEDIUM)));

        DifficultyRecommendation recommendation = service.getRecommendation(childId);

        assertEquals("HARD", recommendation.recommendedDifficulty());
    }

    @Test
    void shouldDowngradeToMediumWhenCurrentIsHardAndScoreIsLow() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime finish = start.plusSeconds(40);
        TaskResultEntity resultEntity = createMockResult(start, finish, 40, taskId);

        when(taskResultRepository.findLastFinishedResults(childId)).thenReturn(List.of(resultEntity));
        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(createRealTask(Difficulty.HARD)));

        DifficultyRecommendation recommendation = service.getRecommendation(childId);

        assertEquals("MEDIUM", recommendation.recommendedDifficulty());
        assertTrue(recommendation.message().contains("снизить уровень сложности"));
    }

    @Test
    void shouldKeepCurrentDifficultyWhenPerformanceIsAverage() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime finish = start.plusSeconds(40);
        TaskResultEntity resultEntity = createMockResult(start, finish, 80, taskId);

        when(taskResultRepository.findLastFinishedResults(childId)).thenReturn(List.of(resultEntity));
        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(createRealTask(Difficulty.MEDIUM)));

        DifficultyRecommendation recommendation = service.getRecommendation(childId);

        assertEquals("MEDIUM", recommendation.recommendedDifficulty());
        assertTrue(recommendation.message().contains("Хороший темп"));
    }

    @Test
    void shouldFallbackToEasyDifficultyIfTaskNotFoundInDb() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime finish = start.plusSeconds(40);
        TaskResultEntity resultEntity = createMockResult(start, finish, 70, taskId);

        when(taskResultRepository.findLastFinishedResults(childId)).thenReturn(List.of(resultEntity));
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        DifficultyRecommendation recommendation = service.getRecommendation(childId);

        assertEquals("EASY", recommendation.recommendedDifficulty());
    }
}
