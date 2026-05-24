package com.umograd.content.application.task.command;

import com.umograd.content.application.dto.TaskDto;

public record EditTasksCommand(
        Long id,
        TaskDto taskDto
) {
}
