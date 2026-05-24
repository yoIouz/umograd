package com.umograd.content.domain.external;

import java.util.List;

/**
 * Универсальное DTO для задач, импортируемых из внешних источников.
 */
public record ExternalTaskDto(
        String sourceId,
        String title,
        String description,
        int minAge,
        int maxAge,
        String difficulty,
        List<String> tags,
        ExternalTaskContentDto content // Теперь содержит список вопросов
) {}

