package com.umograd.content.application.dto;

import java.util.List;

public record QuestionDto(
        String type,
        String question,
        List<String> options,
        String answer,
        String hint
) {}

