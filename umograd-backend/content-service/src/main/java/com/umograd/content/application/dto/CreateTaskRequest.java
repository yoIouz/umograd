package com.umograd.content.application.dto;

import java.util.List;

public record CreateTaskRequest(
        String title,
        String description,
        int minAge,
        int maxAge,
        String difficulty,
        TaskContentDto content
) {}

