package com.umograd.analytic.unit;

import com.umograd.analytic.dto.achievement.AchievementGrantResponse;
import com.umograd.analytic.entity.AchievementEntity;
import com.umograd.analytic.entity.ChildAchievementEntity;
import com.umograd.analytic.entity.task.TaskResultEntity;
import com.umograd.analytic.mapper.AchievementMapper;
import com.umograd.analytic.repository.analytic.AchievementRepository;
import com.umograd.analytic.repository.analytic.ChildAchievementRepository;
import com.umograd.analytic.repository.task.TaskResultRepository;
import com.umograd.analytic.service.impl.DefaultAchievementService;
import com.umograd.analytic.util.ExpressionEvaluator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskAchievementTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private ChildAchievementRepository childAchievementRepository;

    @Mock
    private TaskResultRepository taskResultRepository;

    @Mock
    private AchievementMapper achievementMapper;

    @Spy
    private ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    @InjectMocks
    private DefaultAchievementService service;

    private final Long childId = 4L;

    private AchievementEntity createAchievement(Long id, String expression, Integer value) {
        AchievementEntity a = new AchievementEntity();
        a.setId(id);
        a.setConditionExpression(expression);
        a.setConditionValue(value);
        return a;
    }

    @Test
    void checkAndGrantShouldGrantAchievementWhenConditionsAreMet() {
        String expression = "#results.size() >= #targetValue and #results.?[status == 'DONE' and score >= 100].size() >= #targetValue";
        AchievementEntity achievement = createAchievement(10L, expression, 2);

        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        when(childAchievementRepository.existsByChildIdAndAchievementId(childId, 10L)).thenReturn(false);

        TaskResultEntity r1 = new TaskResultEntity();
        r1.setStatus("DONE");
        r1.setScore(100);

        TaskResultEntity r2 = new TaskResultEntity();
        r2.setStatus("DONE");
        r2.setScore(120);

        when(taskResultRepository.findLastResultsWithWindow(childId, 20)).thenReturn(List.of(r1, r2));

        AchievementGrantResponse response = mock(AchievementGrantResponse.class);
        when(achievementMapper.toGrantResponse(achievement)).thenReturn(response);

        List<AchievementGrantResponse> result = service.checkAndGrant(childId);

        assertEquals(1, result.size());
        assertSame(response, result.get(0));

        ArgumentCaptor<ChildAchievementEntity> captor = ArgumentCaptor.forClass(ChildAchievementEntity.class);
        verify(childAchievementRepository, times(1)).save(captor.capture());

        ChildAchievementEntity saved = captor.getValue();
        assertEquals(childId, saved.getChildId());
        assertSame(achievement, saved.getAchievement());
        assertNotNull(saved.getEarnedAt());
    }

    @Test
    void checkAndGrantShouldNotGrantWhenAlreadyEarned() {
        AchievementEntity achievement = createAchievement(10L, "#results.size() >= #targetValue", 2);
        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        when(childAchievementRepository.existsByChildIdAndAchievementId(childId, 10L)).thenReturn(true);

        List<AchievementGrantResponse> result = service.checkAndGrant(childId);

        assertTrue(result.isEmpty());
        verifyNoInteractions(taskResultRepository, achievementMapper);
        verify(childAchievementRepository, never()).save(any());
    }

    @Test
    void checkAndGrantShouldNotGrantWhenSyntaxErrorInExpression() {
        AchievementEntity achievement = createAchievement(10L, "!!!INVALID_SPEL_SYNTAX!!!", 2);
        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        when(childAchievementRepository.existsByChildIdAndAchievementId(childId, 10L)).thenReturn(false);
        when(taskResultRepository.findLastResultsWithWindow(childId, 20)).thenReturn(Collections.emptyList());

        List<AchievementGrantResponse> result = service.checkAndGrant(childId);

        assertTrue(result.isEmpty());
        verify(childAchievementRepository, never()).save(any());
        verifyNoInteractions(achievementMapper);
    }

    @Test
    void checkAndGrantShouldNotGrantWhenNotEnoughResults() {
        String expression = "#results.size() >= #targetValue and #results.subList(0, #targetValue).stream().allMatch(r -> 'DONE'.equals(r.status) && r.score >= 100)";
        AchievementEntity achievement = createAchievement(10L, expression, 3);

        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        when(childAchievementRepository.existsByChildIdAndAchievementId(childId, 10L)).thenReturn(false);

        TaskResultEntity r1 = new TaskResultEntity();
        r1.setStatus("DONE");
        r1.setScore(100);

        when(taskResultRepository.findLastResultsWithWindow(childId, 20)).thenReturn(List.of(r1));

        List<AchievementGrantResponse> result = service.checkAndGrant(childId);

        assertTrue(result.isEmpty());
        verify(childAchievementRepository, never()).save(any());
        verifyNoInteractions(achievementMapper);
    }

    @Test
    void checkAndGrantShouldNotGrantWhenStatusIsNotDone() {
        String expression = "#results.size() >= #targetValue and #results.subList(0, #targetValue).stream().allMatch(r -> 'DONE'.equals(r.status) && r.score >= 100)";
        AchievementEntity achievement = createAchievement(10L, expression, 1);

        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        when(childAchievementRepository.existsByChildIdAndAchievementId(childId, 10L)).thenReturn(false);

        TaskResultEntity r1 = new TaskResultEntity();
        r1.setStatus("FAILED");
        r1.setScore(100);

        when(taskResultRepository.findLastResultsWithWindow(childId, 20)).thenReturn(List.of(r1));

        List<AchievementGrantResponse> result = service.checkAndGrant(childId);

        assertTrue(result.isEmpty());
        verify(childAchievementRepository, never()).save(any());
    }

    @Test
    void checkAndGrantShouldNotGrantWhenScoreIsLow() {
        String expression = "#results.size() >= #targetValue and #results.subList(0, #targetValue).stream().allMatch(r -> 'DONE'.equals(r.status) && r.score >= 100)";
        AchievementEntity achievement = createAchievement(10L, expression, 1);

        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        when(childAchievementRepository.existsByChildIdAndAchievementId(childId, 10L)).thenReturn(false);

        TaskResultEntity r1 = new TaskResultEntity();
        r1.setStatus("DONE");
        r1.setScore(99);

        when(taskResultRepository.findLastResultsWithWindow(childId, 20)).thenReturn(List.of(r1));

        List<AchievementGrantResponse> result = service.checkAndGrant(childId);

        assertTrue(result.isEmpty());
        verify(childAchievementRepository, never()).save(any());
    }

    @Test
    void getEarnedAchievementIdsShouldReturnMappedIds() {
        AchievementEntity a1 = createAchievement(100L, "true", 1);
        AchievementEntity a2 = createAchievement(200L, "true", 2);

        ChildAchievementEntity ca1 = new ChildAchievementEntity();
        ca1.setAchievement(a1);
        ChildAchievementEntity ca2 = new ChildAchievementEntity();
        ca2.setAchievement(a2);

        when(childAchievementRepository.findAllByChildId(childId)).thenReturn(List.of(ca1, ca2));

        List<Long> result = service.getEarnedAchievementIds(childId);

        assertEquals(2, result.size());
        assertTrue(result.contains(100L));
        assertTrue(result.contains(200L));
    }

    @Test
    void getEarnedAchievementIdsShouldReturnEmptyListWhenNoAchievements() {
        when(childAchievementRepository.findAllByChildId(childId)).thenReturn(Collections.emptyList());

        List<Long> result = service.getEarnedAchievementIds(childId);

        assertTrue(result.isEmpty());
    }
}
