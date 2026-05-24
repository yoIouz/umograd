package com.umograd.content.application.task.command;

import com.umograd.content.application.dto.QuestionDto;
import com.umograd.content.application.dto.TaskContentDto;
import com.umograd.content.application.dto.TaskDto;
import com.umograd.content.application.task.query.TasksHandler;
import com.umograd.content.domain.task.*;
import com.umograd.content.domain.task.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CreateTaskHandler implements TasksHandler<TaskDto, CreateTaskCommand> {
    private final TaskRepository repository;

    public CreateTaskHandler(TaskRepository repository) {
        this.repository = repository;
    }

    public TaskDto handle(CreateTaskCommand cmd) {
        var now = LocalDateTime.now();

        // 1. Маппим вопросы из команды в доменные объекты Question
        List<Question> domainQuestions = cmd.content().questionDtos().stream()
                .map(q -> new Question(
                        q.type(),
                        q.question(),
                        q.options(),
                        q.answer(),
                        q.hint()
                ))
                .collect(Collectors.toList());

        // 2. Создаем задачу с новым TaskContent
        var task = Task.createNew(
                null,
                null,
                new TaskTitle(cmd.title()),
                new TaskDescription(cmd.description()),
                cmd.createdBy(),
                now,
                new AgeRange(cmd.minAge(), cmd.maxAge()),
                Difficulty.valueOf(cmd.difficulty()),
                new TaskContent(domainQuestions) // Передаем список вопросов
        );

        var saved = repository.save(task);

        // 3. Формируем TaskDto, маппим список вопросов обратно в TaskContentDto
        List<QuestionDto> questionDtos = saved.content().questions().stream()
                .map(q -> new QuestionDto(
                        q.contentType(),
                        q.question(),
                        q.options(),
                        q.answer(),
                        q.hint()
                ))
                .collect(Collectors.toList());

        return new TaskDto(
                saved.id() != null ? saved.id().value() : null,
                saved.sourceId(),
                saved.title().value(),
                saved.description().value(),
                saved.ageRange().min(),
                saved.ageRange().max(),
                saved.difficulty().name(),
                saved.createdBy(),
                saved.createdAt(),
                saved.updatedAt(),
                new TaskContentDto(questionDtos) // Теперь DTO содержит список
        );
    }
}
