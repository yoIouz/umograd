package com.umograd.analytic.service.impl;

import com.umograd.analytic.dto.achievement.AchievementGrantResponse;
import com.umograd.analytic.dto.achievement.AchievementResponse;
import com.umograd.analytic.entity.AchievementEntity;
import com.umograd.analytic.entity.ChildAchievementEntity;
import com.umograd.analytic.entity.task.TaskResultEntity;
import com.umograd.analytic.mapper.AchievementMapper;
import com.umograd.analytic.repository.analytic.AchievementRepository;
import com.umograd.analytic.repository.analytic.ChildAchievementRepository;
import com.umograd.analytic.repository.task.TaskRepository;
import com.umograd.analytic.repository.task.TaskResultRepository;
import com.umograd.analytic.service.AchievementService;
import com.umograd.analytic.util.ExpressionEvaluator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultAchievementService implements AchievementService {

    private final AchievementRepository achievementRepository;

    private final ChildAchievementRepository childAchievementRepository;

    private final TaskResultRepository taskResultRepository;

    private final AchievementMapper achievementMapper;

    private final ExpressionEvaluator expressionEvaluator;

    private final TaskRepository taskRepository;

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

    @Override
    @Transactional
    public List<AchievementGrantResponse> checkAndGrant(Long childId) {
        return achievementRepository.findAll().stream()
                .filter(a -> !childAchievementRepository.existsByChildIdAndAchievementId(childId, a.getId()))
                .filter(a -> isConditionMet(childId, a))
                .map(a -> saveAndMap(childId, a))
                .collect(Collectors.toList());
    }

    private boolean isConditionMet(Long childId, AchievementEntity achievement) {
        List<TaskResultEntity> lastResults = taskResultRepository.findLastResultsWithWindow(childId, 20);
        if (lastResults.isEmpty()) {
            return false;
        }

        int target = achievement.getConditionValue();
        String name = achievement.getName();

        int currentCorrectStreak = 0;
        int maxCorrectStreak = 0;
        int currentQuizStreak = 0;
        int maxQuizStreak = 0;
        int totalDoneCount = 0;
        int totalScore = 0;
        Set<String> uniqueDifficulties = new HashSet<>();

        for (TaskResultEntity r : lastResults) {
            if (!"DONE".equals(r.getStatus())) {
                currentCorrectStreak = 0;
                currentQuizStreak = 0;
                continue;
            }

            totalDoneCount++;

            if (r.getScore() != null) {
                totalScore += r.getScore();
            }

            if (r.getScore() != null && r.getScore() >= 100) {
                currentCorrectStreak++;
                maxCorrectStreak = Math.max(maxCorrectStreak, currentCorrectStreak);
            } else {
                currentCorrectStreak = 0;
            }

            var taskOpt = taskRepository.findById(r.getTaskId());
            if (taskOpt.isPresent()) {
                var task = taskOpt.get();
                uniqueDifficulties.add(task.getDifficulty().toString());

                String desc = task.getDescription() != null ? task.getDescription().toLowerCase() : "";
                if (desc.contains("викторина") || desc.contains("quiz")) {
                    currentQuizStreak++;
                    maxQuizStreak = Math.max(maxQuizStreak, currentQuizStreak);
                } else {
                    currentQuizStreak = 0;
                }
            }
        }

        return switch (name) {
            case "Снайпер" -> maxCorrectStreak >= target;
            case "Алмазный ум" -> totalDoneCount >= target;
            case "Король викторин" -> maxQuizStreak >= target;
            case "Учёный исследователь" -> uniqueDifficulties.size() >= 3;
            case "Золотая медаль" -> totalScore >= target;
            case "Любимец команды" -> totalDoneCount >= target;
            default -> false;
        };
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
