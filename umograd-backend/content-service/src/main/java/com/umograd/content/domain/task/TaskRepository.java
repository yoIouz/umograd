package com.umograd.content.domain.task;

public interface TaskRepository {
    Task save(Task task);
    Task findById(TaskId id);
    java.util.List<Task> findAll();
    void delete(TaskId id);
}
