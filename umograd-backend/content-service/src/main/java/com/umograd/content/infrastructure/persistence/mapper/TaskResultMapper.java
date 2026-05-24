package com.umograd.content.infrastructure.persistence.mapper;

import com.umograd.content.domain.result.*;
import com.umograd.content.domain.task.TaskId;
import com.umograd.content.infrastructure.persistence.jpa.TaskResultJpaEntity;

public class TaskResultMapper {
    public static TaskResultJpaEntity toJpa(TaskResult r) {
        var e = new TaskResultJpaEntity();
        if (r.getId() != null) e.setId(r.getId().value());
        e.setTaskId(r.getTaskId().value());
        e.setChildId(r.getChildId());
        e.setStatus(r.getStatus().name());
        e.setScore(r.getScore());
        e.setFinishedAt(r.getFinishedAt());
        e.setStartedAt(r.getStartedAt());
        return e;
    }

    public static TaskResult toDomain(TaskResultJpaEntity e) {
        return TaskResult.restore(
                e.getId() != null ? new TaskResultId(e.getId()) : null,
                new TaskId(e.getTaskId()),
                e.getChildId(),
                TaskResultStatus.valueOf(e.getStatus()),
                e.getScore(),
                e.getFinishedAt(),
                e.getStartedAt()
        );
    }
}
