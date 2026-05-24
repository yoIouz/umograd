package com.umograd.content.infrastructure.repository;

import com.umograd.content.domain.task.*;
import com.umograd.content.infrastructure.persistence.jpa.SpringTaskRepository;
import com.umograd.content.infrastructure.persistence.jpa.TaskJpaEntity;
import com.umograd.content.infrastructure.persistence.mapper.TaskMapper;

import java.util.List;

public class TaskRepositoryImpl implements TaskRepository {

    private final SpringTaskRepository jpa;

    public TaskRepositoryImpl(SpringTaskRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Task save(Task task) {
        TaskJpaEntity saved = jpa.save(TaskMapper.toJpa(task));
        return TaskMapper.toDomain(saved);
    }

    @Override
    public Task findById(TaskId id) {
        return jpa.findById(id.value())
                .map(TaskMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    @Override
    public List<Task> findAll() {
        return jpa.findAll().stream().map(TaskMapper::toDomain).toList();
    }

    @Override
    public void delete(TaskId id) {
        jpa.deleteById(id.value());
    }
}
