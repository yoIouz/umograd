package com.umograd.content.application.task.command;

import com.umograd.content.application.task.query.TasksHandler;
import com.umograd.content.domain.task.TaskId;
import com.umograd.content.domain.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteTaskHandler implements TasksHandler<Void, DeleteTaskCommand> {

    private final TaskRepository taskRepository;

    @Override
    public Void handle(DeleteTaskCommand query) {
        taskRepository.delete(new TaskId(query.id()));
        return null;
    }
}
