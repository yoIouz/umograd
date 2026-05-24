package com.umograd.content.application.task.query;

import com.umograd.content.application.dto.QuestionDto;
import com.umograd.content.application.dto.TaskContentDto;
import com.umograd.content.application.dto.TaskDto;
import com.umograd.content.domain.task.Difficulty;
import com.umograd.content.domain.task.Task;
import com.umograd.content.domain.task.TaskRepository;

import java.util.List;

public class ListTasksForChildHandler {
    private final TaskRepository repository;

    public ListTasksForChildHandler(TaskRepository repository) {
        this.repository = repository;
    }

    public List<TaskDto> handle(ListTasksForChildQuery query) {
        return repository.findAll().stream()
                .filter(task -> query.age() >= task.ageRange().min() && query.age() <= task.ageRange().max())
                .filter(task -> query.difficulty() == null || task.difficulty() == Difficulty.valueOf(query.difficulty()))
                .map(this::toDto)
                .toList();
    }

    private TaskDto toDto(Task task) {
        List<QuestionDto> questionDtos = task.content().questions().stream()
                .map(q -> new QuestionDto(
                        q.contentType(),
                        q.question(),
                        q.options(),
                        q.answer(),
                        q.hint()
                ))
                .toList();

        return new TaskDto(
                task.id() != null ? task.id().value() : null,
                task.sourceId(),
                task.title().value(),
                task.description().value(),
                task.ageRange().min(),
                task.ageRange().max(),
                task.difficulty().name(),
                task.createdBy(),
                task.createdAt(),
                task.updatedAt(),
                new TaskContentDto(questionDtos)
        );
    }
}
