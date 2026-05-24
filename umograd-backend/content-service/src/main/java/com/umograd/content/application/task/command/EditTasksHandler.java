package com.umograd.content.application.task.command;

import com.umograd.content.application.dto.TaskDto;
import com.umograd.content.application.task.query.TasksHandler;
import com.umograd.content.domain.task.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EditTasksHandler implements TasksHandler<Void, EditTasksCommand> {

    private final TaskRepository taskRepository;

    @Override
    public Void handle(EditTasksCommand query) {
        TaskDto dto = query.taskDto();
        Task existingTask = taskRepository.findById(new TaskId(query.id()));
        if (existingTask == null) {
            return null;
        }
        List<Question> newQuestions = dto.content().questionDtos().stream()
                .map(q -> new Question(
                        q.type(),
                        q.question(),
                        q.options(),
                        q.answer(),
                        q.hint()
                ))
                .toList();

        existingTask.rename(new TaskTitle(dto.title()), new TaskDescription(dto.description()), LocalDateTime.now());
        existingTask.updateAgeRange(new AgeRange(dto.minAge(), dto.maxAge()), LocalDateTime.now());
        existingTask.changeDifficulty(Difficulty.valueOf(dto.difficulty()), LocalDateTime.now());
        existingTask.updateContent(new TaskContent(newQuestions), LocalDateTime.now());

        taskRepository.save(existingTask);
        return null;
    }
}
