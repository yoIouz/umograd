package com.umograd.content.application.dto;

import java.util.List;

public record TaskContentDto(
        List<QuestionDto> questionDtos
) {}
