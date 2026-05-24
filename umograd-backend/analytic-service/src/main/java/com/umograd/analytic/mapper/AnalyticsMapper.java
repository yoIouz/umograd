package com.umograd.analytic.mapper;

import com.umograd.analytic.dto.TaskAnalyticsResponse;
import com.umograd.analytic.entity.task.TaskJpaEntity;
import com.umograd.analytic.entity.task.TaskResultEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public abstract class AnalyticsMapper {

    @Mapping(target = "taskId", source = "id")
    @Mapping(target = "averageScore", ignore = true)
    @Mapping(target = "totalAttempts", ignore = true)
    @Mapping(target = "recommendation", ignore = true)
    public abstract TaskAnalyticsResponse toResponse(TaskJpaEntity task);

    @AfterMapping
    protected void calculateStats(TaskJpaEntity task, @MappingTarget TaskAnalyticsResponse response) {
        List<TaskResultEntity> results = task.getResults();

        if (results == null || results.isEmpty()) {
            response.setAverageScore(0.0);
            response.setTotalAttempts(0L);
            response.setRecommendation("No data");
            return;
        }

        double avg = results.stream()
                .mapToInt(TaskResultEntity::getScore)
                .average()
                .orElse(0.0);

        long count = results.size();

        response.setAverageScore(avg);
        response.setTotalAttempts(count);

        if (avg > 90 && count > 5) {
            response.setRecommendation("UP_DIFFICULTY");
        } else if (avg < 50 && count > 5) {
            response.setRecommendation("DOWN_DIFFICULTY");
        } else {
            response.setRecommendation("STAY");
        }
    }
}
