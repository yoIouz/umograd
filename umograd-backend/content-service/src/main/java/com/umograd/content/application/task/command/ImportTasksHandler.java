package com.umograd.content.application.task.command;

import com.umograd.content.application.dto.QuestionDto;
import com.umograd.content.application.dto.TaskContentDto;
import com.umograd.content.application.dto.TaskDto;
import com.umograd.content.application.task.query.TasksHandler;
import com.umograd.content.domain.external.ContentProvider;
import com.umograd.content.domain.external.ExternalTaskDto;
import com.umograd.content.domain.task.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class ImportTasksHandler implements TasksHandler<List<TaskDto>, ImportTasksCommand> {

    private final TaskRepository taskRepository;
    private final Map<String, ContentProvider> providers;

    public ImportTasksHandler(TaskRepository taskRepository, Map<String, ContentProvider> providers) {
        this.taskRepository = Objects.requireNonNull(taskRepository);
        this.providers = Objects.requireNonNull(providers);
    }

    public List<TaskDto> handle(ImportTasksCommand cmd) {
        ContentProvider provider = providers.get(cmd.providerName());
        if (provider == null) {
            throw new IllegalArgumentException("Unknown provider: " + cmd.providerName());
        }

        List<ExternalTaskDto> externalTasks = provider.fetchTasks(cmd.topic(), cmd.limit());
        LocalDateTime now = LocalDateTime.now();

        return externalTasks.stream()
                .map(et -> toDomainTask(et, cmd.createdBy(), now))
                .map(taskRepository::save)
                .map(this::toDto)
                .toList();
    }

    private Task toDomainTask(ExternalTaskDto et, String createdBy, LocalDateTime now) {
        // Маппим список вопросов из внешнего DTO в доменные объекты Question
        List<Question> domainQuestions = et.content().questionDtos().stream()
                .map(q -> new Question(
                        q.type(),
                        q.question(),
                        q.options(),
                        q.answer(),
                        q.hint()
                ))
                .toList();

        return Task.createNew(
                null,
                et.sourceId(),
                new TaskTitle(et.title()),
                new TaskDescription(et.description()),
                createdBy,
                now,
                new AgeRange(et.minAge(), et.maxAge()),
                Difficulty.valueOf(et.difficulty().toUpperCase()),
                new TaskContent(domainQuestions) // Передаем список
        );
    }

    private TaskDto toDto(Task task) {
        // Маппим вопросы из домена в QuestionDto
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
                new TaskContentDto(questionDtos) // Обертка над списком
        );
    }

    public TaskDto saveSingle(TaskDto dto, String createdBy) {
        LocalDateTime now = LocalDateTime.now();

        List<Question> domainQuestions = dto.content().questionDtos().stream()
                .map(q -> new Question(
                        q.type(),
                        q.question(),
                        q.options(),
                        q.answer(),
                        q.hint()
                ))
                .toList();

        Task task = Task.createNew(
                null,
                dto.sourceId(),
                new TaskTitle(dto.title()),
                new TaskDescription(dto.description()),
                createdBy,
                now,
                new AgeRange(dto.minAge(), dto.maxAge()),
                Difficulty.valueOf(dto.difficulty().toUpperCase()),
                new TaskContent(domainQuestions)
        );
        Task saved = taskRepository.save(task);
        return toDto(saved);
    }

    // preview оставляем без изменений, так как он работает напрямую с внешними DTO
    public List<ExternalTaskDto> preview(String providerName, String topic, int limit) {
        ContentProvider provider = providers.get(providerName);
        if (provider == null) {
            throw new IllegalArgumentException("Unknown provider: " + providerName);
        }
        return provider.fetchTasks(topic, limit);
    }
}
