package com.umograd.analytic.mapper;

import com.umograd.analytic.dto.achievement.AchievementGrantResponse;
import com.umograd.analytic.dto.achievement.AchievementResponse;
import com.umograd.analytic.entity.AchievementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AchievementMapper {

    @Mapping(target = "newlyEarned", constant = "true")
    AchievementGrantResponse toGrantResponse(AchievementEntity achievement);

    AchievementResponse toResponse(AchievementEntity achievement);

    List<AchievementResponse> toResponseList(List<AchievementEntity> achievements);
}
