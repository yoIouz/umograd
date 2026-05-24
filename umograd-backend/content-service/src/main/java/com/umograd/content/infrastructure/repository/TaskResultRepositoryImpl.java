package com.umograd.content.infrastructure.repository;

import com.umograd.content.domain.result.*;
import com.umograd.content.domain.task.TaskId;
import com.umograd.content.infrastructure.persistence.jpa.SpringTaskResultRepository;
import com.umograd.content.infrastructure.persistence.mapper.TaskResultMapper;

import java.util.List;
import java.util.Optional;

public class TaskResultRepositoryImpl implements TaskResultRepository {

    private final SpringTaskResultRepository jpa;

    public TaskResultRepositoryImpl(SpringTaskResultRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public TaskResult save(TaskResult result) {
        var saved = jpa.save(TaskResultMapper.toJpa(result));
        return TaskResultMapper.toDomain(saved);
    }

    @Override
    public Optional<TaskResult> findById(TaskResultId id) {
        return jpa.findById(id.value())
                .map(TaskResultMapper::toDomain);
    }

    @Override
    public List<TaskResult> findByChildId(Long childId) {
        return jpa.findByChildId(childId).stream().map(TaskResultMapper::toDomain).toList();
    }

    @Override
    public List<TaskResult> findByTaskId(TaskId taskId) {
        return jpa.findByTaskId(taskId.value()).stream().map(TaskResultMapper::toDomain).toList();
    }
}
