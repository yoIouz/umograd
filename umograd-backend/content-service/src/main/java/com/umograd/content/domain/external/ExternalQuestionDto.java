package com.umograd.content.domain.external;

import java.util.List;

public record ExternalQuestionDto(
        String type,
        String question,
        List<String> options,
        String answer,
        String hint
) {}
