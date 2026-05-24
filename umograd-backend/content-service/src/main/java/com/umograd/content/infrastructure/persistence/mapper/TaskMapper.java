package com.umograd.content.infrastructure.persistence.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umograd.content.domain.task.*;
import com.umograd.content.infrastructure.persistence.jpa.TaskJpaEntity;
import com.umograd.content.infrastructure.persistence.jpa.TaskQuestionJpaEntity;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static TaskJpaEntity toJpa(Task task) {
        var e = new TaskJpaEntity();
        if (task.id() != null) {
            e.setId(task.id().value());
        }
        e.setSourceId(task.sourceId());
        e.setTitle(task.title().value());
        e.setDescription(task.description().value());
        e.setCreatedBy(task.createdBy());
        e.setCreatedAt(task.createdAt());
        e.setUpdatedAt(task.updatedAt());
        e.setMinAge(task.ageRange().min());
        e.setMaxAge(task.ageRange().max());
        e.setDifficulty(task.difficulty());

        if (task.content() != null && task.content().questions() != null) {
            List<TaskQuestionJpaEntity> questionEntities = task.content().questions().stream()
                    .map(q -> {
                        var qe = new TaskQuestionJpaEntity();
                        qe.setContentType(q.contentType());
                        qe.setQuestion(q.question());
                        qe.setAnswer(q.answer());
                        qe.setTask(e);
                        qe.setHint(q.hint());
                        try {
                            qe.setOptions(objectMapper.writeValueAsString(q.options()));
                        } catch (JsonProcessingException ex) {
                            throw new RuntimeException("Failed to serialize options", ex);
                        }
                        return qe;
                    })
                    .collect(Collectors.toList());
            e.setQuestions(questionEntities);
        }
        return e;
    }

    public static Task toDomain(TaskJpaEntity e) {
        List<Question> domainQuestions = Collections.emptyList();

        if (e.getQuestions() != null) {
            domainQuestions = e.getQuestions().stream()
                    .map(qe -> {
                        List<String> options = Collections.emptyList();
                        if (qe.getOptions() != null) {
                            try {
                                options = objectMapper.readValue(
                                        qe.getOptions(),
                                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
                                );
                            } catch (IOException ex) {
                                throw new RuntimeException("Failed to deserialize options", ex);
                            }
                        }
                        return new Question(
                                qe.getContentType(),
                                qe.getQuestion(),
                                options,
                                qe.getAnswer(),
                                qe.getHint()
                        );
                    })
                    .collect(Collectors.toList());
        }

        return Task.createNew(
                e.getId() != null ? new TaskId(e.getId()) : null,
                e.getSourceId(),
                new TaskTitle(e.getTitle()),
                new TaskDescription(e.getDescription()),
                e.getCreatedBy(),
                e.getCreatedAt(),
                new AgeRange(e.getMinAge(), e.getMaxAge()),
                e.getDifficulty(),
                new TaskContent(domainQuestions)
        );
    }
}