package com.umograd.content.domain.result;

import com.umograd.content.domain.task.TaskId;

import java.util.List;
import java.util.Optional;

public interface TaskResultRepository {
    TaskResult save(TaskResult result);
    Optional<TaskResult> findById(TaskResultId id);
    List<TaskResult> findByChildId(Long childId);
    List<TaskResult> findByTaskId(TaskId taskId);
}
