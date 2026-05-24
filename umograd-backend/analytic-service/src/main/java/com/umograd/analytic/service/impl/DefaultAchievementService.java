package com.umograd.analytic.service.impl;

import com.umograd.analytic.dto.achievement.AchievementGrantResponse;
import com.umograd.analytic.dto.achievement.AchievementResponse;
import com.umograd.analytic.entity.AchievementEntity;
import com.umograd.analytic.entity.ChildAchievementEntity;
import com.umograd.analytic.entity.task.TaskResultEntity;
import com.umograd.analytic.mapper.AchievementMapper;
import com.umograd.analytic.repository.analytic.AchievementRepository;
import com.umograd.analytic.repository.analytic.ChildAchievementRepository;
import com.umograd.analytic.repository.task.TaskResultRepository;
import com.umograd.analytic.service.AchievementService;
import com.umograd.analytic.util.ExpressionEvaluator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultAchievementService implements AchievementService {

    private final AchievementRepository achievementRepository;

    private final ChildAchievementRepository childAchievementRepository;

    private final TaskResultRepository taskResultRepository;

    private final AchievementMapper achievementMapper;

    private final ExpressionEvaluator expressionEvaluator;

    @Override
    @Transactional
    public List<AchievementGrantResponse> checkAndGrant(Long childId) {
        return achievementRepository.findAll().stream()
                .filter(a -> !childAchievementRepository.existsByChildIdAndAchievementId(childId, a.getId()))
                .filter(a -> isConditionMet(childId, a))
                .map(a -> saveAndMap(childId, a))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getEarnedAchievementIds(Long childId) {
        return childAchievementRepository.findAllByChildId(childId).stream()
                .map(ach -> ach.getAchievement().getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<AchievementResponse> getAchievements() {
        return achievementMapper.toResponseList(achievementRepository.findAll());
    }


    private boolean isConditionMet(Long childId, AchievementEntity achievement) {
        List<TaskResultEntity> lastResults = taskResultRepository.findLastResultsWithWindow(childId, 20);
        Map<String, Object> variables = Map.of(
                "results", lastResults,
                "targetValue", achievement.getConditionValue()
        );
        return expressionEvaluator.evaluateBoolean(achievement.getConditionExpression(), variables);
    }

    private AchievementGrantResponse saveAndMap(Long childId, AchievementEntity achievement) {
        ChildAchievementEntity grant = new ChildAchievementEntity();
        grant.setChildId(childId);
        grant.setAchievement(achievement);
        grant.setEarnedAt(LocalDateTime.now());
        childAchievementRepository.save(grant);
        return achievementMapper.toGrantResponse(achievement);
    }
}
