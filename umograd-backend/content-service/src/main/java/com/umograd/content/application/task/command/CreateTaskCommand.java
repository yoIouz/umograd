package com.umograd.content.application.task.command;

import com.umograd.content.application.dto.TaskContentDto;

public record CreateTaskCommand(
        String title,
        String description,
        String createdBy,
        int minAge,
        int maxAge,
        String difficulty,
        TaskContentDto content
) {}
