package com.umograd.analytic.mapper;

import com.umograd.analytic.dto.ParentAgeLimitCustomResponse;
import com.umograd.analytic.dto.ParentAgeLimitResponse;
import com.umograd.analytic.entity.limit.ParentAgeLimitEntity;
import com.umograd.analytic.entity.limit.ParentChildCustomLimitEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LimitMapper {

    ParentAgeLimitResponse toDto(ParentAgeLimitEntity entity);

    List<ParentAgeLimitResponse> toListDto(List<ParentAgeLimitEntity> list);

    @Mapping(target = "customMinutes", source = "customMinutes")
    ParentAgeLimitCustomResponse toCustomDto(ParentChildCustomLimitEntity entity);

    List<ParentAgeLimitCustomResponse> toListCustomDto(List<ParentChildCustomLimitEntity> list);
}
