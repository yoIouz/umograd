package com.umograd.content.domain.result;

import com.umograd.content.domain.task.TaskId;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class TaskResult {
    private final TaskResultId id;
    private final TaskId taskId;
    private final Long childId;
    private TaskResultStatus status;
    private Integer score;
    private LocalDateTime finishedAt;
    private LocalDateTime startedAt;

    private TaskResult(TaskResultId id,
                       TaskId taskId,
                       Long childId,
                       TaskResultStatus status,
                       Integer score,
                       LocalDateTime finishedAt,
                       LocalDateTime startedAt) {
        this.id = id;
        this.taskId = Objects.requireNonNull(taskId);
        this.childId = Objects.requireNonNull(childId);
        this.status = Objects.requireNonNull(status);
        this.score = score;
        this.finishedAt = finishedAt;
        this.startedAt = startedAt;
    }

    public static TaskResult createNew(TaskId taskId, Long childId) {
        return new TaskResult(null, taskId, childId,
                TaskResultStatus.IN_PROGRESS, null, null, LocalDateTime.now());
    }

    public static TaskResult restore(TaskResultId id,
                                     TaskId taskId,
                                     Long childId,
                                     TaskResultStatus status,
                                     Integer score,
                                     LocalDateTime finishedAt,
                                     LocalDateTime startedAt) {
        return new TaskResult(id, taskId, childId, status, score, finishedAt, startedAt);
    }

    public void markFinished(int score, LocalDateTime finishedAt) {
        if (this.status == TaskResultStatus.DONE) {
            throw new IllegalStateException("Задание уже завершено");
        }
        this.status = TaskResultStatus.DONE;
        this.score = score;
        this.finishedAt = finishedAt;
    }

    public void updateScore(int newScore) {
        if (this.status != TaskResultStatus.DONE) {
            throw new IllegalStateException("Нельзя обновить оценку для незавершённого задания");
        }
        this.score = newScore;
    }
}
