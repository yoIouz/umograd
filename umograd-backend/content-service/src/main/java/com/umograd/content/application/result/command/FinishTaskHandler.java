package com.umograd.content.application.result.command;

import com.umograd.content.application.dto.TaskResultDto;
import com.umograd.content.domain.result.TaskResultId;
import com.umograd.content.domain.result.TaskResultRepository;

import java.time.LocalDateTime;

public class FinishTaskHandler {

    private final TaskResultRepository repository;

    public FinishTaskHandler(TaskResultRepository repository) {
        this.repository = repository;
    }

    public TaskResultDto handle(FinishTaskCommand cmd) {
        var result = repository.findById(new TaskResultId(cmd.taskResultId()))
                .orElseThrow(() -> new IllegalArgumentException("TaskResult not found"));

        if (!result.getChildId().equals(cmd.childId())) {
            throw new SecurityException("Child cannot finish someone else's task");
        }

//        if (cmd.score() != null && cmd.score() == 0 && !cmd.allowZero()) {
//            throw new IllegalArgumentException("Finishing with zero is not allowed without confirmation");
//        }

        result.markFinished(cmd.score(), LocalDateTime.now());
        var saved = repository.save(result);

        return new TaskResultDto(
                saved.getId() != null ? saved.getId().value() : null,
                saved.getTaskId().value(),
                saved.getChildId(),
                saved.getStatus().name(),
                saved.getScore(),
                saved.getFinishedAt() != null ? saved.getFinishedAt().toString() : null,
                saved.getStartedAt() != null ? saved.getStartedAt().toString() : null
        );
    }
}