package com.umograd.content.domain.external;

import java.util.List;

/**
 * DTO для содержимого внешней задачи.
 */
public record ExternalTaskContentDto(
        List<ExternalQuestionDto> questionDtos
) {}
