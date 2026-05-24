package com.umograd.content.application.task.query;

import com.umograd.content.application.dto.QuestionDto;
import com.umograd.content.application.dto.TaskContentDto;
import com.umograd.content.application.dto.TaskDto;
import com.umograd.content.domain.task.Task;
import com.umograd.content.domain.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListTasksHandler implements TasksHandler<List<TaskDto>, ListTasksQuery> {

    private final TaskRepository repository;

    @Override
    public List<TaskDto> handle(ListTasksQuery query) {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public TaskDto toDto(Task task) {
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