package com.umograd.content.application.result.query;

import com.umograd.content.application.dto.TaskResultDto;
import com.umograd.content.domain.result.TaskResultRepository;

import java.util.List;

public class GetChildResultsHandler {
    private final TaskResultRepository repository;

    public GetChildResultsHandler(TaskResultRepository repository) {
        this.repository = repository;
    }

    public List<TaskResultDto> handle(GetChildResultsQuery q) {
        return repository.findByChildId(q.childId()).stream()
                .map(r -> new TaskResultDto(
                        r.getId() != null ? r.getId().value() : null,
                        r.getTaskId().value(),
                        r.getChildId(),
                        r.getStatus().name(),
                        r.getScore(),
                        r.getFinishedAt() != null ? r.getFinishedAt().toString() : null,
                        r.getStartedAt() != null ? r.getStartedAt().toString() : null
                ))
                .toList();
    }
}
