package com.umograd.content.application.dto;

import java.time.LocalDateTime;

public record TaskDto(
        Long id,             // внутренний ID в БД
        String sourceId,     // внешний ID (например, из OpenTDB)
        String title,
        String description,
        int minAge,
        int maxAge,
        String difficulty,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        TaskContentDto content
) {}
