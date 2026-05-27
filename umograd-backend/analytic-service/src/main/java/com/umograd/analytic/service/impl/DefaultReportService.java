package com.umograd.analytic.service.impl;

import com.umograd.analytic.dto.report.ChildReportDto;
import com.umograd.analytic.entity.task.TaskJpaEntity;
import com.umograd.analytic.entity.task.TaskResultEntity;
import com.umograd.analytic.repository.task.TaskRepository;
import com.umograd.analytic.repository.task.TaskResultRepository;
import com.umograd.analytic.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultReportService implements ReportService {

    private final TaskResultRepository taskResultRepository;

    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public ChildReportDto getChildReport(Long childId, String period) {
        List<TaskResultEntity> allResults = taskResultRepository.findAllByChildId(childId);

        java.time.LocalDateTime cutoffDate = switch (period.toLowerCase()) {
            case "day" -> java.time.LocalDateTime.now().minusHours(24);
            case "week" -> java.time.LocalDateTime.now().minusWeeks(1);
            default -> java.time.LocalDateTime.now().minusMonths(1);
        };

        List<TaskResultEntity> results = allResults.stream()
                .filter(r -> r.getFinishedAt() != null && r.getFinishedAt().isAfter(cutoffDate))
                .toList();

        List<ChildReportDto.ProgressPoint> progressPoints = new java.util.ArrayList<>();
        List<ChildReportDto.TimePoint> timePoints = new java.util.ArrayList<>();
        Map<String, Long> difficultyStats = new java.util.HashMap<>();

        difficultyStats.put("EASY", 0L);
        difficultyStats.put("MEDIUM", 0L);
        difficultyStats.put("HARD", 0L);

        java.util.Map<String, java.util.List<TaskResultEntity>> groupedByDate = results.stream()
                .filter(r -> "DONE".equals(r.getStatus()) && r.getFinishedAt() != null)
                .collect(java.util.stream.Collectors.groupingBy(r ->
                        r.getFinishedAt().toLocalDate().toString()
                ));

        for (var entry : groupedByDate.entrySet()) {
            String date = entry.getKey();
            List<TaskResultEntity> dayResults = entry.getValue();

            int avgScore = (int) dayResults.stream()
                    .mapToInt(r -> r.getScore() != null ? r.getScore() : 0)
                    .average().orElse(0.0);
            progressPoints.add(new ChildReportDto.ProgressPoint(date, avgScore));

            long avgSeconds = (long) dayResults.stream()
                    .mapToLong(r -> java.time.Duration.between(r.getStartedAt(), r.getFinishedAt()).toSeconds())
                    .average().orElse(0.0);
            timePoints.add(new ChildReportDto.TimePoint(date, avgSeconds));
        }

        for (TaskResultEntity r : results) {
            if ("DONE".equals(r.getStatus())) {
                Optional<TaskJpaEntity> task = taskRepository.findById(r.getTaskId());
                if (task.isPresent()) {
                    String diff = task.get().getDifficulty().toString();
                    difficultyStats.put(diff, difficultyStats.getOrDefault(diff, 0L) + 1);
                }
            }
        }

        progressPoints.sort(java.util.Comparator.comparing(ChildReportDto.ProgressPoint::date));
        timePoints.sort(java.util.Comparator.comparing(ChildReportDto.TimePoint::date));

        return new ChildReportDto(progressPoints, timePoints, difficultyStats);
    }
}
